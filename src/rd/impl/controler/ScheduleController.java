package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import rd.dto.ActivityDto;
import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.MeetingDto;
import rd.dto.NoteDto;
import rd.dto.ProductDto;
import rd.dto.SaleTargetDto;
import rd.dto.ScheduleTaskDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ActivityService;
import rd.spec.service.CompanyService;
import rd.spec.service.ContactService;
import rd.spec.service.MeetingService;
import rd.spec.service.NoteService;
import rd.spec.service.SaleTargetService;
import rd.spec.service.ScheduleTaskService;
import rd.spec.service.UserService;
import rd.utils.Pair;

@Named
@ConversationScoped
public class ScheduleController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject MeetingService meetingService;
	@Inject SessionManager sessionManager;
	@Inject CompanyService compService;
	@Inject NoteService noteService;
	@Inject SaleTargetService stService;
	@Inject ScheduleTaskService taskService;
	@Inject ActivityService actService;
	@Inject UserService userService;

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if(!conversation.isTransient()){
			conversation.end();
		}
	}

	public void reload() {
		conversationBegin();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public ScheduleModel getModel() throws IOException {
		if (model == null) {
			model = new DefaultScheduleModel();
			for (MeetingDto dto: getEvents()) {
				DefaultScheduleEvent dse = new DefaultScheduleEvent(dto.getDetail() + ". Client: " + dto.getCustomer().getName(), dto.getFrom(), dto.getTo());
				dse.setId(String.valueOf(dto.getSeq()));
				model.addEvent(dse);
			}
		}
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	private Date from;
	private Date to;
	private String title;
	private ScheduleModel model;
	private String companyName;

	public void addNewEvent() throws IOException {
		// reload();
		if (newMeeting.getTo().getTime() < newMeeting.getFrom().getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid date info", null);
			return;
		}
		if (contactName == null || contactName.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Customer name is required", null);
			return;
		}
		ContactDto targetContact = contactService.getContactById(Integer.parseInt(contactName.split("[()]")[1]));
		newMeeting.setContact(targetContact);
		newMeeting.setSalesperson(sessionManager.getLoginUser());
		if (isMeetingAtCustomer()) {
			newMeeting.setLocation(targetContact.getAddress());
		}

		meetingService.addMeeting(newMeeting);

		if (isToday(newMeeting)) {
			events.add(newMeeting);
		}

		sessionManager.addGlobalMessageInfo("New schedule event added", null);
		if (!checkConflict()) {
			sessionManager.addGlobalMessageWarn("Warning: Conflict detected in your chedule.", null);
		}
		from = null;
		to = null;
		title = "";
		companyName = "";
		addMeetingMode = false;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public boolean checkConflict() throws IOException {
		List<MeetingDto> temp = meetingService.getMeetingByUser(sessionManager.getLoginUser().getId());
		List<Pair<Date, Integer>> time = new ArrayList<Pair<Date, Integer>>();
		for (MeetingDto dto: temp) {
			time.add(new Pair<Date, Integer>(dto.getFrom(), 1));
			time.add(new Pair<Date, Integer>(dto.getTo(), 2));
		}
		Collections.sort(time, new Comparator<Pair<Date, Integer>>() {
			public int compare(Pair<Date, Integer> first, Pair<Date, Integer> second) {
				if (first.getFirst().compareTo(second.getFirst()) != 0) {
					return first.getFirst().compareTo(second.getFirst());
				} else {
					return first.getSecond().compareTo(second.getSecond());
				}
			}
		});
		int count = 0;
		for (int i = 0; i < time.size(); i++) {
			Pair<Date, Integer> p = time.get(i);
			if (p.getSecond() == 1) {
				count++;
			} else {
				count --;
			}
			if (count == 2) {
				for (int k = i + 1; k < time.size(); k++) {
					if (time.get(k).getFirst().compareTo(p.getFirst()) == 0 && time.get(k).getSecond() == 2) {
						break;
					} else if (time.get(k).getFirst().compareTo(p.getFirst()) > 0) {
						System.out.println("false here : " +time.get(k).getFirst());
						return false;
					}
				}
			}
		}
		return true;
	}

	public String logout() throws IOException {
		conversationEnd();
		System.out.println("ended");
		sessionManager.logoff();
		System.out.println("logged out");
		return "../portal.jsf?faces-redirect=true";
	}

	public List<MeetingDto> getEvents() throws IOException {
		if (events == null) {
			events = meetingService.getMeetingToday(sessionManager.getLoginUser().getId());
		}
		return events;
	}

	public void setEvents(List<MeetingDto> events) {
		this.events = events;
	}

	private List<MeetingDto> events;

	public void remove(MeetingDto meeting) throws IOException {
		for (int i = events.size() - 1; i >= 0; i--) {
			if (events.get(i).getSeq() == meeting.getSeq()) {
				events.remove(i);
				meetingService.deleteMeeting(meeting.getSeq());
				break;
			}
		}
		sessionManager.addGlobalMessageInfo("Event removed.", null);
	}

	public List<NoteDto> getNotes() throws IOException {
		if (notes == null || notes.size() == 0) {
			notes = noteService.getRecentNote(sessionManager.getLoginUser().getId());
		}
		return notes;
	}

	public void setNotes(List<NoteDto> notes) {
		this.notes = notes;
	}

	public int getNewNotes() throws IOException {
		if (newNotes == 0) {
			for (NoteDto note: getNotes()) {
				if (note.getStatus().equalsIgnoreCase("unread")) {
					newNotes++;
				}
			}
		}
		return newNotes;
	}

	public void setNewNotes(int newNotes) {
		this.newNotes = newNotes;
	}

	private List<NoteDto> notes;
	private int newNotes;

	public void markRead(NoteDto note) throws IOException {
		if (note.getStatus().equalsIgnoreCase("unread"))
			newNotes--;
		note.setStatus("READ");
		noteService.updateNote(note);
		for (int i = 0; i < notes.size(); i++) {
			if (notes.get(i).getSeq() == note.getSeq()) {
				notes.set(i, note);
				break;
			}
		}
	}

	public String getResponseDetail() {
		return responseDetail;
	}

	public void setResponseDetail(String responseDetail) {
		this.responseDetail = responseDetail;
	}

	public boolean isRespondMode() {
		return respondMode;
	}

	public void setRespondMode(boolean respondMode) {
		this.respondMode = respondMode;
	}

	private boolean respondMode;
	private String responseDetail;
	private UserDto targetUser = null;
	private NoteDto selectedNote;

	public void startRespond(NoteDto note) {
		respondMode = true;
		targetUser = note.getFromUser();
		selectedNote = note;
	}

	public void respond() throws IOException {
		if (responseDetail == null || responseDetail.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Response can't be empty", null);
			return;
		}

		int seq = noteService.getSeq();
		NoteDto note = new NoteDto(seq, sessionManager.getLoginUser(), targetUser, responseDetail, new Date());
		noteService.addNote(note);
		respondMode = false;
		markRead(selectedNote);
	}

	public NoteDto getSelectedNote() {
		return selectedNote;
	}

	public void setSelectedNote(NoteDto selectedNote) {
		this.selectedNote = selectedNote;
	}

	public SaleTargetDto getStd() throws IOException {
		if (std == null) {
			std = stService.getSaleTarget(sessionManager.getLoginUser().getId());
		}
		return std;
	}

	public void setStd(SaleTargetDto std) {
		this.std = std;
	}

	public List<ScheduleTaskDto> getTasks() throws IOException {
		if (tasks == null) {
			tasks = taskService.getByUserToday(sessionManager.getLoginUser().getId());
			for (ScheduleTaskDto t: tasks) {
				if (t.getTime().getTime() < (new Date()).getTime() && t.getStatus().equalsIgnoreCase("pending")) {
					t.setStatus("Overdue");
				}
			}
			List<ScheduleTaskDto> overdue = taskService.getByUserAndStatus(sessionManager.getLoginUser().getId(), "Overdue");
			overdue.addAll(tasks);
			tasks = overdue;
		}
		return tasks;
	}

	public void setTasks(List<ScheduleTaskDto> tasks) {
		this.tasks = tasks;
	}

	private SaleTargetDto std;
	private List<ScheduleTaskDto> tasks;
	private ScheduleTaskDto editingTask;
	private boolean editTaskMode = false;

	public void cancelRespond() {
		respondMode = false;
	}

	public ScheduleTaskDto getEditingTask() {
		if (editingTask == null) {
			editingTask = new ScheduleTaskDto();
		}
		return editingTask;
	}

	public void setEditingTask(ScheduleTaskDto editingTask) {
		this.editingTask = editingTask;
	}

	public void startEditTask(ScheduleTaskDto task) {
		editTaskMode = true;
		editingTask = task;
	}

	public boolean isEditTaskMode() {
		return editTaskMode;
	}

	public void setEditTaskMode(boolean editTaskMode) {
		this.editTaskMode = editTaskMode;
	}

	public void cancelEditTask() throws IOException {
		editTaskMode = false;
		for (int i = tasks.size() - 1; i >= 0; i--) {
			if (tasks.get(i).getSeq() == editingTask.getSeq()) {
				tasks.set(i, taskService.getById(editingTask.getSeq()));
				break;
			}
		}
		editingTask = new ScheduleTaskDto();
	}

	public void editTask() throws IOException {
		taskService.updateEvent(editingTask);
		sessionManager.addGlobalMessageInfo("Task updated", null);
		editTaskMode = false;
	}

	public void deleteTask(ScheduleTaskDto task) throws IOException {
		for (int i = tasks.size() - 1; i >= 0; i--) {
			if (tasks.get(i).getSeq() == task.getSeq()) {
				tasks.remove(i);
				break;
			}
		}
		taskService.deleteEvent(task.getSeq());
	}

	public double getPercentage() throws IOException {
		percentage = Math.floor((double)getStd().getCurrent() / (double) getStd().getTarget() * 10000) / 100;
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public boolean isAddMeetingMode() {
		return addMeetingMode;
	}

	public void setAddMeetingMode(boolean addMeetingMode) {
		this.addMeetingMode = addMeetingMode;
	}

	private double percentage;

	private boolean addMeetingMode = false;
	private boolean addTaskMode = false;
	private ScheduleTaskDto newTask;

	public void startAddMeeting() {
		addMeetingMode = true;
	}

	public void cancelAddMeeting() {
		addMeetingMode = false;
	}

	public boolean isAddTaskMode() {
		return addTaskMode;
	}

	public void setAddTaskMode(boolean addTaskMode) {
		this.addTaskMode = addTaskMode;
	}

	public void startAddTask() {
		addTaskMode = true;
	}

	public void cancelAddTask() {
		addTaskMode = false;
	}

	@Inject ContactService contactService;

	public void addNewTask() throws NumberFormatException, IOException {
		if (contactName_2 == null || contactName_2.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Customer name is required", null);
			return;
		}
		if (newTask.getCategory() == null || newTask.getCategory().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Action is required", null);
			return;
		}
		if (newTask.getTime() == null) {
			sessionManager.addGlobalMessageFatal("Time is required", null);
			return;
		}

		ContactDto cont = contactService.getContactById(Integer.parseInt(contactName_2.split("[()]")[1]));
		newTask.setContact(cont);
		newTask.setSeq(taskService.getSeq());
		newTask.setUsername(sessionManager.getLoginUser().getId());

		taskService.addEvent(newTask);

		sessionManager.addGlobalMessageInfo("New task added", null);
		if (isToday(newTask)) {
			tasks.add(newTask);
		}

		addTaskMode = false;
		contactName_2 = "";
		newTask = new ScheduleTaskDto();
	}

	private boolean isToday(ScheduleTaskDto newTask) {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		Calendar nextDay = new GregorianCalendar();
		nextDay.set(Calendar.HOUR_OF_DAY, 0);
		nextDay.set(Calendar.MINUTE, 0);
		nextDay.set(Calendar.SECOND, 0);
		nextDay.set(Calendar.MILLISECOND, 0);
		nextDay.add(Calendar.DAY_OF_MONTH, 1);

		return (newTask.getTime().getTime() >= date.getTime().getTime()) && (newTask.getTime().getTime() < nextDay.getTime().getTime());
	}

	private boolean isToday(MeetingDto meeting) {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		Calendar nextDay = new GregorianCalendar();
		nextDay.set(Calendar.HOUR_OF_DAY, 0);
		nextDay.set(Calendar.MINUTE, 0);
		nextDay.set(Calendar.SECOND, 0);
		nextDay.set(Calendar.MILLISECOND, 0);
		nextDay.add(Calendar.DAY_OF_MONTH, 1);

		return (meeting.getFrom().getTime() >= date.getTime().getTime() && meeting.getFrom().getTime() < nextDay.getTime().getTime());
	}

	public ScheduleTaskDto getNewTask() {
		if (newTask == null) {
			newTask = new ScheduleTaskDto();
		}
		return newTask;
	}

	public void setNewTask(ScheduleTaskDto newTask) {
		this.newTask = newTask;
	}

	public String getCompanyName_2() {
		return companyName_2;
	}

	public void setCompanyName_2(String companyName_2) {
		this.companyName_2 = companyName_2;
	}

	public boolean isAddActivityMode() {
		return addActivityMode;
	}

	public void setAddActivityMode(boolean addActivityMode) {
		this.addActivityMode = addActivityMode;
	}

	private String companyName_2;
	private boolean addActivityMode = false;
	private ActivityDto newAct;

	public void startAddNewAct(ScheduleTaskDto task) throws IOException {
		// getNewAct().setCustomer(task.getCustomer());
		newAct.setContact(task.getContact());
		newAct.setStatus("Contacted");
		addActivityMode = true;

		firstMeeting = new MeetingDto();
		firstMeeting.setContact(task.getContact());
		firstMeeting.setSalesperson(sessionManager.getLoginUser());
	}

	public void addNewAct() throws IOException {
		int seq = actService.getSeq();
		newAct.setSeq(seq);
		newAct.setSalesperson(sessionManager.getLoginUser());
		actService.addActivity(getNewAct());

		firstMeeting.setDetail(newAct.getRemark());
		meetingService.addMeeting(firstMeeting);
		firstMeeting = new MeetingDto();

		sessionManager.addGlobalMessageInfo("New activity added", null);
		addActivityMode = false;
	}

	public void cancelAddNewAct() {
		addActivityMode = false;
	}

	public ActivityDto getNewAct() {
		if (newAct == null) {
			newAct = new ActivityDto();
		}
		return newAct;
	}

	public void setNewAct(ActivityDto newAct) {
		this.newAct = newAct;
	}

	public MeetingDto getFirstMeeting() {
		return firstMeeting;
	}

	public void setFirstMeeting(MeetingDto firstMeeting) {
		this.firstMeeting = firstMeeting;
	}

	public boolean isViewContactMode() {
		return viewContactMode;
	}

	public void setViewContactMode(boolean viewContactMode) {
		this.viewContactMode = viewContactMode;
	}

	private MeetingDto firstMeeting = new MeetingDto();
	private boolean viewContactMode = false;
	private String contactNumber;

	public void startViewContact(ScheduleTaskDto task) {
		contactNumber = task.getCustomer().getPhone();
		viewContactMode = true;
	}

	public void cancelViewContact() {
		viewContactMode = false;
		contactNumber = "";
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public boolean isMeetingAtCustomer() {
		return meetingAtCustomer;
	}

	public void setMeetingAtCustomer(boolean meetingAtCustomer) {
		this.meetingAtCustomer = meetingAtCustomer;
	}

	public MeetingDto getNewMeeting() {
		return newMeeting;
	}

	public void setNewMeeting(MeetingDto newMeeting) {
		this.newMeeting = newMeeting;
	}

	public String getContactName_2() {
		return contactName_2;
	}

	public void setContactName_2(String contactName_2) {
		this.contactName_2 = contactName_2;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public ActivityDto getTempAct() {
		return tempAct;
	}

	public void setTempAct(ActivityDto tempAct) {
		this.tempAct = tempAct;
	}

	private boolean meetingAtCustomer = true;
	private MeetingDto newMeeting = new MeetingDto();
	private String contactName_2;
	private String contactName;
	private ActivityDto tempAct;

	public void startViewDealDetail(ScheduleTaskDto task) throws IOException {
		tempAct = actService.getById(task.getActId());
		List<ProductDto> prods = actService.getProductByDeal(task.getActId());
		tempAct.setProducts(prods);
	}

	public Date getDateSearch() {
		return dateSearch;
	}

	public void setDateSearch(Date dateSearch) {
		this.dateSearch = dateSearch;
	}

	public boolean isScheduleSearchMode() {
		return scheduleSearchMode;
	}

	public void setScheduleSearchMode(boolean scheduleSearchMode) {
		this.scheduleSearchMode = scheduleSearchMode;
	}

	private Date dateSearch;
	private boolean scheduleSearchMode;
	private List<MeetingDto> searchedMeetings;

	public void searchSchedule() throws IOException {
		// searchedMeetings = meetingService.getMeetingByDayAndUser(sessionManager.getLoginUser().getId(), dateSearch);
		searchedMeetings = meetingService.getByIntervalAndUser(dateSearch, dateSearchTo, sessionManager.getLoginUser().getId());
		scheduleSearchMode = true;
	}
	public void resetSearchSchedule() {
		scheduleSearchMode = false;
		dateSearch = null;
	}

	public List<MeetingDto> getSearchedMeetings() {
		return searchedMeetings;
	}

	public void setSearchedMeetings(List<MeetingDto> searchedMeetings) {
		this.searchedMeetings = searchedMeetings;
	}

	public Date getTaskDateSearch() {
		return taskDateSearch;
	}

	public void setTaskDateSearch(Date taskDateSearch) {
		this.taskDateSearch = taskDateSearch;
	}

	public boolean isTaskSearchMode() {
		return taskSearchMode;
	}

	public void setTaskSearchMode(boolean taskSearchMode) {
		this.taskSearchMode = taskSearchMode;
	}

	public Date getTaskDateTo() {
		return taskDateTo;
	}

	public void setTaskDateTo(Date taskDateTo) {
		this.taskDateTo = taskDateTo;
	}

	private Date taskDateTo;
	private Date taskDateSearch;
	private boolean taskSearchMode;
	private List<ScheduleTaskDto> searchedTasks;

	public void searchTask() throws IOException {
		// setSearchedTasks(taskService.getByUser(sessionManager.getLoginUser().getId(), taskDateSearch));
		taskSearchMode = true;
		searchedTasks = taskService.getByIntervalAndUser(taskDateSearch, taskDateTo, sessionManager.getLoginUser().getId());
	}
	public void resetSearchTask() {
		taskSearchMode = false;
		taskDateSearch = null;
	}

	public List<ScheduleTaskDto> getSearchedTasks() {
		return searchedTasks;
	}

	public void setSearchedTasks(List<ScheduleTaskDto> searchedTasks) {
		this.searchedTasks = searchedTasks;
	}

	public void startViewDetail(MeetingDto meeting) throws IOException {
		tempAct = actService.getById(meeting.getActId());
		System.out.println(tempAct.getProducts().size());
		List<ProductDto> prods = actService.getProductByDeal(meeting.getActId());
		tempAct.setProducts(prods);
	}

	public void markAsDone(ScheduleTaskDto task) throws IOException {
		task.setStatus("Done");
		taskService.updateEvent(task);
	}

	public String getReminder() throws IOException {
		for (MeetingDto event: getEvents()) {
			long difference = event.getFrom().getTime() - (new Date()).getTime();
			System.out.println(difference);
			if (0 < difference && difference < 1000*30*60) {
				reminder = "You will have a meeting in 30 minutes";
				sessionManager.addGlobalMessageInfo(reminder, null);
				System.out.println("ScheduleController.getReminder() 1");
				break;
			} else if (0 < difference && difference < 1000*60*60) {
				reminder = "You will have a meeting in 1 hour";
				sessionManager.addGlobalMessageInfo(reminder, null);
				System.out.println("ScheduleController.getReminder() 2");
				break;
			} else if (0 < difference && difference < 1000*120*60) {
				reminder = "You will have a meeting in 2 hour";
				sessionManager.addGlobalMessageInfo(reminder, null);
				System.out.println("ScheduleController.getReminder() 3");
				break;
			}
		}
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public Date getDateSearchTo() {
		return dateSearchTo;
	}

	public void setDateSearchTo(Date dateSearchTo) {
		this.dateSearchTo = dateSearchTo;
	}

	public boolean isSomeMeetingSelected() {
		return someMeetingSelected;
	}

	public void setSomeMeetingSelected(boolean someMeetingSelected) {
		this.someMeetingSelected = someMeetingSelected;
	}

	private String reminder;
	private Date dateSearchTo;

	private boolean someMeetingSelected;

	public void updateButtons() {
		someMeetingSelected = false;
		if (scheduleSearchMode)
			for (MeetingDto dto: searchedMeetings) {
				if (dto.isSelected()) {
					someMeetingSelected = true;
					break;
				}
			}
		else
			for (MeetingDto dto: events) {
				if (dto.isSelected()) {
					someMeetingSelected = true;
					break;
				}
			}
	}

	public String getReassigneeName() {
		return reassigneeName;
	}

	public void setReassigneeName(String reassigneeName) {
		this.reassigneeName = reassigneeName;
	}

	public boolean isReassignMode() {
		return reassignMode;
	}

	public void setReassignMode(boolean reassignMode) {
		this.reassignMode = reassignMode;
	}

	private boolean reassignMode;
	private String reassigneeName;
	private boolean someTaskSelected;

	public void startReassign() {
		reassignMode = true;
	}
	public void confirmReassignment() throws IOException {
		UserDto newAssignee = userService.findUserById(reassigneeName.split("[()]")[1]);
		if (scheduleSearchMode) {
			for (MeetingDto meeting: searchedMeetings) {
				if (meeting.isSelected()) {
					meeting.setSalesperson(newAssignee);
					meetingService.editMeeting(meeting);
				}
			}
			for (int i = searchedMeetings.size() - 1; i >= 0; i--) {
				if (searchedMeetings.get(i).isSelected()) {
					searchedMeetings.remove(i);
				}
			}
		} else {
			for (MeetingDto meeting: events) {
				if (meeting.isSelected()) {
					meeting.setSalesperson(newAssignee);
					meetingService.editMeeting(meeting);
				}
			}
			for (int i = events.size() - 1; i >= 0; i--) {
				if (events.get(i).isSelected()) {
					events.remove(i);
				}
			}
		}
		sessionManager.addGlobalMessageInfo("Reassigned meetings", null);
		reassignMode = false;
	}
	public void cancelReassignment() {
		reassignMode = false;
	}

	public void startReassignTask() {
		reassignMode = true;
		someMeetingSelected = false;
	}

	public boolean isSomeTaskSelected() {
		return someTaskSelected;
	}

	public void setSomeTaskSelected(boolean someTaskSelected) {
		this.someTaskSelected = someTaskSelected;
	}
	public void updateTaskButtons() {
		someTaskSelected = false;
		if (taskSearchMode) {
			for (ScheduleTaskDto dto: searchedTasks) {
				if (dto.isSelected()) {
					someTaskSelected = true;
					break;
				}
			}
		} else {
			for (ScheduleTaskDto dto: tasks) {
				if (dto.isSelected()) {
					someTaskSelected = true;
					break;
				}
			}
		}
		System.out.println("ScheduleController.updateTaskButtons()");
		System.out.println(someTaskSelected);
	}

	public void confirmTaskReassignment() throws IOException {
		UserDto newAssignee = userService.findUserById(reassigneeName.split("[()]")[1]);
		if (taskSearchMode) {
			for (ScheduleTaskDto dto: searchedTasks) {
				if (dto.isSelected()) {
					dto.setUsername(newAssignee.getId());
					taskService.updateEvent(dto);
				}
			}
			for (int i = searchedTasks.size() - 1; i >= 0; i--) {
				if (searchedTasks.get(i).isSelected()) {
					searchedTasks.remove(i);
				}
			}
		} else {
			for (ScheduleTaskDto dto: tasks) {
				if (dto.isSelected()) {
					dto.setUsername(newAssignee.getId());
					taskService.updateEvent(dto);
				}
			}
			for (int i = tasks.size() - 1; i >= 0; i--) {
				if (tasks.get(i).isSelected()) {
					tasks.remove(i);
				}
			}
		}
		reassignMode = false;
	}
}
