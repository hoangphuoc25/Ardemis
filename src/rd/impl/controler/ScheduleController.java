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
import rd.dto.MeetingDto;
import rd.dto.NoteDto;
import rd.dto.SaleTargetDto;
import rd.dto.ScheduleTaskDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ActivityService;
import rd.spec.service.CompanyService;
import rd.spec.service.MeetingService;
import rd.spec.service.NoteService;
import rd.spec.service.SaleTargetService;
import rd.spec.service.ScheduleTaskService;
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
		if (to.getTime() < from.getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid date info", null);
			return;
		}
		System.out.println("ScheduleController.addNewEvent()");
		System.out.println(companyName);
		CompanyDto comp = compService.getById(Integer.parseInt(companyName.split("[()]")[1]));
		System.out.println(Integer.parseInt(companyName.split("[()]")[1]));
		MeetingDto newMeeting = new MeetingDto(meetingService.getSeq(), from, to, title, comp, sessionManager.getLoginUser());
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

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
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

	public void addNewTask() throws NumberFormatException, IOException {
		if (companyName_2 == null || companyName_2.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Company name invalid", null);
			return;
		}

		CompanyDto comp = compService.getById(Integer.parseInt(companyName_2.split("[()]")[1]));
		newTask.setCustomer(comp);
		newTask.setSeq(taskService.getSeq());
		newTask.setUsername(sessionManager.getLoginUser().getId());

		taskService.addEvent(newTask);

		sessionManager.addGlobalMessageInfo("New task added", null);
		if (isToday(newTask)) {
			tasks.add(newTask);
		}

		addTaskMode = false;
		companyName_2 = "";
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

	public void startAddNewAct(ScheduleTaskDto task) {
		getNewAct().setCustomer(task.getCustomer());
		newAct.setStatus("Contacted");
		addActivityMode = true;
	}

	public void addNewAct() throws IOException {
		int seq = actService.getSeq();
		newAct.setSeq(seq);
		newAct.setSalesperson(sessionManager.getLoginUser());
		actService.addActivity(getNewAct());
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
}
