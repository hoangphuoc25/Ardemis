package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.ActivityDto;
import rd.dto.CallReportDto;
import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.MeetingDto;
import rd.dto.ProductDto;
import rd.dto.SaleTargetDto;
import rd.dto.ScheduleTaskDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ActivityService;
import rd.spec.service.CallReportService;
import rd.spec.service.CompanyService;
import rd.spec.service.InvoiceService;
import rd.spec.service.MeetingService;
import rd.spec.service.ProductService;
import rd.spec.service.SaleTargetService;
import rd.spec.service.ScheduleTaskService;

@Named
@ConversationScoped
public class ActivityController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject CompanyService compService;
	@Inject ActivityService actService;
	@Inject SessionManager sessionManager;
	@Inject InvoiceService invoiceService;
	@Inject ProductService prodService;
	@Inject SaleTargetService stService;
	@Inject MeetingService meetingService;

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

	public ActivityDto getNewAct() {
		if (newAct == null) {
			newAct = new ActivityDto();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			String date = sdf.format(new Date());
			newAct.setRemark(date + ":\n");
		}
		return newAct;
	}

	public void setNewAct(ActivityDto newAct) {
		this.newAct = newAct;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public List<String> getStatusOptions() {
		if (statusOptions == null) {
			statusOptions = new ArrayList<String>();
			statusOptions.add("Contacted");
			statusOptions.add("Negotiating");
			statusOptions.add("Completed");
			statusOptions.add("Halted");
		}
		return statusOptions;
	}

	public void setStatusOptions(List<String> statusOptions) {
		this.statusOptions = statusOptions;
	}

	public List<ActivityDto> getAllAct() throws IOException {
		if (allAct == null) {
			allAct = actService.getByUser(sessionManager.getLoginUser().getId());
		}
		return allAct;
	}

	public void setAllAct(List<ActivityDto> allAct) {
		this.allAct = allAct;
	}

	private String compName;
	private ActivityDto newAct;
	private List<String> statusOptions;
	private List<ActivityDto> allAct;
	private List<String> statusOptionsEditBox;

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp =  compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public void addActivity() throws IOException {
		if (compName == null || compName.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Please enter company name", null);
			return;
		}
		if (newAct.getStartDate().getTime() > (new Date()).getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid date", null);
			return;
		}

		int seq = actService.getSeq();
		newAct.setSeq(seq);
		newAct.setSalesperson(sessionManager.getLoginUser());
		CompanyDto comp = compService.getById(Integer.parseInt(compName.split("[()]")[1]));
		newAct.setCustomer(comp);
		actService.addActivity(newAct);
		allAct.add(newAct);
		newAct = new ActivityDto();
		sessionManager.addGlobalMessageFatal("New sale activity added", null);
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public ActivityDto getSelectedAct() {
		if (selectedAct == null) {
			selectedAct = new ActivityDto();
		}
		return selectedAct;
	}

	public void setSelectedAct(ActivityDto selectedAct) {
		this.selectedAct = selectedAct;
	}

	private boolean editMode = false;
	private ActivityDto selectedAct;

	public void startEdit(ActivityDto act) {
		editMode = true;
		selectedAct = act;
		String content = act.getRemark();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String date = sdf.format(new Date());
		content += "\n\n" + date + ":\n";
		selectedAct.setRemark(content);
	}

	public void editAct() throws IOException {
		actService.updateActivity(selectedAct);
		for (int i = 0; i < allAct.size(); i++) {
			if (allAct.get(i).getSeq() == selectedAct.getSeq()) {
				allAct.set(i, selectedAct);
				break;
			}
		}
		editMode = false;
		sessionManager.addGlobalMessageInfo("Sale activity updated", null);
	}

	public boolean isAddInvoiceMode() {
		return addInvoiceMode;
	}

	public void setAddInvoiceMode(boolean addInvoiceMode) {
		this.addInvoiceMode = addInvoiceMode;
	}

	private boolean addInvoiceMode = false;

	public void startAddNewPurchase(ActivityDto act) {
		System.out.println("ActivityController.startAddNewPurchase()");
		addInvoiceMode = true;
		selectedAct = act;
	}

	private InvoiceDto newInvoice;
	private List<ProductDto> selectedProducts;
	private String search;

	public void addNewInvoice() throws IOException {
		if (getSelectedProducts() == null || getSelectedProducts().size() == 0) {
			sessionManager.addGlobalMessageFatal("Product list empty", null);
			return;
		}
		if (newInvoice.getPurchaseDate() == null) {
			sessionManager.addGlobalMessageFatal("Invalid date", null);
			return;
		}
		getNewInvoice().setSeq(invoiceService.getSeq());

		getNewInvoice().setCustomer(selectedAct.getCustomer());
		double amount = 0;
		for (ProductDto dto: getSelectedProducts()) {
			amount += dto.getPrice();
		}
		getNewInvoice().setProducts(getSelectedProducts());
		getNewInvoice().setAmount(amount);
		newInvoice.setSalesperson(sessionManager.getLoginUser());
		invoiceService.addInvoice(getNewInvoice());

		getStd().setCurrent(getStd().getCurrent() + (int) Math.ceil(amount));
		stService.updateSaleTarget(std);

		sessionManager.addGlobalMessageInfo("New purchase record added", null);
		setSearch("");
		setSelectedProducts(new ArrayList<ProductDto>());
		newInvoice = new InvoiceDto();

		selectedAct.setStatus("Completed");
		editAct();
		addInvoiceMode = false;

		if (!showingMode.equalsIgnoreCase("all")) {
			for (int i = allAct.size() - 1; i >= 0; i--) {
				if (allAct.get(i).getSeq() == selectedAct.getSeq()) {
					allAct.remove(i);
					break;
				}
			}
		}
	}

	public InvoiceDto getNewInvoice() {
		if (newInvoice == null)
			newInvoice = new InvoiceDto();
		return newInvoice;
	}

	public void setNewInvoice(InvoiceDto newInvoice) {
		this.newInvoice = newInvoice;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<String> suggest(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public List<ProductDto> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<ProductDto> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public void select() throws IOException {
		if (getSelectedProducts() == null) {
			setSelectedProducts(new ArrayList<ProductDto>());
		}
		int seq = Integer.parseInt(getSearch().split("[()]")[1]);
		ProductDto prod = prodService.getProductById(seq);
		for (int i = 0; i < selectedProducts.size(); i++) {
			if (selectedProducts.get(i).getSeq() == seq)
				return;
		}
		selectedProducts.add(prod);
		search = "";
	}

	public void cancelEditAct() throws IOException {
		editMode = false;
		for (int i = 0; i < allAct.size(); i++) {
			if (allAct.get(i).getSeq() == selectedAct.getSeq()) {
				ActivityDto temp = actService.getById(selectedAct.getSeq());
				allAct.set(i, temp);
				break;
			}
		}
		selectedAct = new ActivityDto();
	}

	public void cancelAddInvoice() {
		addInvoiceMode = false;
		newInvoice = new InvoiceDto();
		selectedProducts = new ArrayList<ProductDto>();
		search = "";
	}

	public void deleteSelected() {
		for (Iterator<ProductDto> iterator = getSelectedProducts().iterator(); iterator.hasNext();) {
		    ProductDto t = iterator.next();
		    if (t.isSelected()) {
		        iterator.remove();
		    }
		}
		System.out.println("ActivityController.deleteSelected()");
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

	public List<String> getStatusOptionsEditBox() {
		if (statusOptionsEditBox == null) {
			statusOptionsEditBox = new ArrayList<String>();
			statusOptionsEditBox.add("Contacted");
			statusOptionsEditBox.add("Negotiating");
			statusOptionsEditBox.add("Halted");
		}
		return statusOptionsEditBox;
	}

	public void setStatusOptionsEditBox(List<String> statusOptionsEditBox) {
		this.statusOptionsEditBox = statusOptionsEditBox;
	}

	public MeetingDto getNewMeeting() {
		if (newMeeting == null) {
			newMeeting = new MeetingDto();
		}
		return newMeeting;
	}

	public void setNewMeeting(MeetingDto newMeeting) {
		this.newMeeting = newMeeting;
	}

	public boolean isAddMeetingMode() {
		return addMeetingMode;
	}

	public void setAddMeetingMode(boolean addMeetingMode) {
		this.addMeetingMode = addMeetingMode;
	}

	private SaleTargetDto std;
	private MeetingDto newMeeting;
	private boolean addMeetingMode = false;

	public void startAddMeeting(ActivityDto act) throws IOException {
		newMeeting = new MeetingDto();
		newMeeting.setCustomer(act.getCustomer());
		newMeeting.setSalesperson(sessionManager.getLoginUser());
		addMeetingMode = true;
	}

	public void addNewMeeting() throws IOException {
		if (newMeeting.getFrom() == null || newMeeting.getTo() == null) {
			sessionManager.addGlobalMessageFatal("Start time and end time are required", null);
			return;
		}
		if (newMeeting.getFrom().getTime() > newMeeting.getTo().getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid time", null);
			return;
		}
		meetingService.addMeeting(newMeeting);
		sessionManager.addGlobalMessageInfo("New meeting added", null);
		addMeetingMode = false;
	}

	public String getShowingMode() {
		return showingMode;
	}

	public void setShowingMode(String showingMode) {
		this.showingMode = showingMode;
	}

	private String showingMode = "all";

	public void updateAllAct() throws IOException {
		if (showingMode.equalsIgnoreCase("all")) {
			allAct = actService.getByUser(sessionManager.getLoginUser().getId());
		} else {
			allAct = actService.findByStatus(showingMode, sessionManager.getLoginUser().getId());
		}
	}

	public void deleteActivity(ActivityDto act) throws IOException {
		for (int i = allAct.size() - 1; i >= 0; i--) {
			if (allAct.get(i).getSeq() == act.getSeq()) {
				allAct.remove(i);
				break;
			}
		}
		actService.deleteActivity(act);
	}

	public boolean isAddTaskMode() {
		return addTaskMode;
	}

	public void setAddTaskMode(boolean addTaskMode) {
		this.addTaskMode = addTaskMode;
	}

	private boolean addTaskMode = false;
	private ScheduleTaskDto newTask;

	public void startAddTask(ActivityDto act) throws IOException {
		addTaskMode = true;
		newTask.setCustomer(act.getCustomer());
		newTask.setUsername(sessionManager.getLoginUser().getId());
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

	@Inject ScheduleTaskService taskService;
	public void addTask() throws IOException {
		taskService.addEvent(newTask);
		newTask = new ScheduleTaskDto();
		sessionManager.addGlobalMessageInfo("New task added", null);
		addTaskMode = false;
	}

	public void cancelAddTask() {
		addTaskMode = false;
		newTask = new ScheduleTaskDto();
	}

	public void cancelAddNewMeeting() {
		addMeetingMode = false;
		newMeeting = new MeetingDto();
	}


	private List<ScheduleTaskDto> relatedTask;
	private List<CallReportDto> relatedCallRecords;
	private boolean viewRelatedTask = false;

	@Inject CallReportService crService;

	public void startFindRelatedTask(ActivityDto act) throws IOException {
		viewRelatedTask = true;
		relatedCallRecords = crService.getByCompanyId(act.getCustomer().getSeq());
		relatedTask = taskService.getByCompany(act.getCustomer().getSeq());
	}

	public void cancelViewRelatedTask() {
		viewRelatedTask = false;
		relatedCallRecords = new ArrayList<CallReportDto>();
		relatedTask = new ArrayList<ScheduleTaskDto>();
	}

	public List<CallReportDto> getRelatedCallRecords() {
		return relatedCallRecords;
	}

	public void setRelatedCallRecords(List<CallReportDto> relatedCallRecords) {
		this.relatedCallRecords = relatedCallRecords;
	}

	public List<ScheduleTaskDto> getRelatedTask() {
		return relatedTask;
	}

	public void setRelatedTask(List<ScheduleTaskDto> relatedTask) {
		this.relatedTask = relatedTask;
	}

	public boolean isViewRelatedTask() {
		return viewRelatedTask;
	}

	public void setViewRelatedTask(boolean viewRelatedTask) {
		this.viewRelatedTask = viewRelatedTask;
	}
}

