package rd.impl.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;

import rd.dto.ActivityDto;
import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.MeetingDto;
import rd.dto.NoteDto;
import rd.dto.ProductDto;
import rd.dto.PromotionDto;
import rd.dto.SaleExpenseDto;
import rd.dto.SaleTargetDto;
import rd.dto.UserDto;
import rd.dto.WrStatisticsDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ActivityService;
import rd.spec.service.CompanyService;
import rd.spec.service.ContactService;
import rd.spec.service.InvoiceService;
import rd.spec.service.MeetingService;
import rd.spec.service.NoteService;
import rd.spec.service.ProductService;
import rd.spec.service.PromotionService;
import rd.spec.service.SaleExpenseService;
import rd.spec.service.SaleTargetService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class ManagerController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject UserService userService;
	@Inject SessionManager sessionManager;
	@Inject MeetingService meetingService;
	@Inject CompanyService compService;
	@Inject NoteService noteService;
	@Inject SaleTargetService stService;

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

	public List<UserDto> getTeamRoster() throws IOException {
		if (team >= 0) {
			teamRoster = userService.getUserByTeamLazy(team);
		}
		return teamRoster;
	}

	public void setTeamRoster(List<UserDto> teamRoster) {
		this.teamRoster = teamRoster;
	}

	public UserDto getSelectedSale() {
		return selectedSale;
	}

	public void setSelectedSale(UserDto selectedSale) {
		this.selectedSale = selectedSale;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	private List<UserDto> teamRoster;
	private UserDto selectedSale;
	private int team;

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}

	public String link(UserDto sale) {
		conversationEnd();
		return "../faces/employee.jsf?id="+sale.getId()+"&faces-redirect=true";
	}

	public List<MeetingDto> getEventList() throws IOException {
		if (eventList == null || eventList.size() == 0) {
			eventList = meetingService.getMeetingToday();
		}
		return eventList;
	}

	public void setEventList(List<MeetingDto> eventList) {
		this.eventList = eventList;
	}

	private List<MeetingDto> eventList;

	public void viewSchedule(UserDto sale) throws IOException {
		conversationEnd();

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("employeeSchedule.jsf?id="+sale.getId());
	}

	private ScheduleModel model;

	public ScheduleModel getModel() throws IOException {
		model = new DefaultScheduleModel();
		List<MeetingDto> temp = meetingService.getMeetingByUser(empId);
		System.out.println(empId);
		System.out.println(temp.size());
		for (MeetingDto dto: temp)
			model.addEvent(new DefaultScheduleEvent(dto.getDetail(), dto.getFrom(), dto.getTo()));
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	private String empId;

	public void updateEmpId(UserDto sale) {
		this.empId = sale.getId();
		System.out.println(empId);
	}

	public boolean isAssignMode() {
		return assignMode;
	}

	public void setAssignMode(boolean assignMode) {
		this.assignMode = assignMode;
	}

	private boolean assignMode = false;
	private String assignComName;
	private UserDto assignee;

	public void startAssign(UserDto assignee) {
		assignMode = true;
		this.setAssignee(assignee);
	}

	public String getAssignComName() {
		return assignComName;
	}

	public void setAssignComName(String assignComName) {
		this.assignComName = assignComName;
	}

	public UserDto getAssignee() {
		return assignee;
	}

	public void setAssignee(UserDto assignee) {
		this.assignee = assignee;
	}

	public void assignSale() throws IOException {
		int seq = noteService.getSeq();
		NoteDto note = new NoteDto(seq, sessionManager.getLoginUser(), assignee, taskDetail, new Date());
		noteService.addNote(note);
		assignMode = false;
		sessionManager.addGlobalMessageInfo("Assigned", null);
	}

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public void cancel() {
		assignMode = false;
	}


	public boolean isViewExpenseMode() {
		return viewExpenseMode;
	}

	public void setViewExpenseMode(boolean viewExpenseMode) {
		this.viewExpenseMode = viewExpenseMode;
	}

	private boolean viewExpenseMode = false;

	@Inject SaleExpenseService seService;
	public void viewExpense(UserDto user) throws IOException {
		reload();
		assignMode = false;
		empId = "";
		viewExpenseMode = true;
		expList = seService.getBySalepersonId(user.getId());
	}

	public List<SaleExpenseDto> getExpList() {
		return expList;
	}

	public void setExpList(List<SaleExpenseDto> expList) {
		this.expList = expList;
	}

	private List<SaleExpenseDto> expList;

	public void cancelViewExpense() {
		viewExpenseMode = false;
	}

	public StreamedContent getFile() throws ParseException {
		reload();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
		Row rr = sheet.createRow(0);
		Cell cc = rr.createCell(0);
		cc.setCellValue("Name");
		cc = rr.createCell(1);
		cc.setCellValue("Receipt Date");
		cc = rr.createCell(2);
		cc.setCellValue("Purpose");
		cc = rr.createCell(3);
		cc.setCellValue("Receipt No");
		cc = rr.createCell(4);
		cc.setCellValue("Amount");

		for (int i = 0; i < expList.size(); i++) {
			Row row = sheet.createRow(i+1);
			Cell cell = row.createCell(0);
			cell.setCellValue(expList.get(i).getSalesperson().getName());
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			cell.setCellValue(sdf.format(expList.get(i).getReceiptDate()));
			cell = row.createCell(2);
			cell.setCellValue(expList.get(i).getPurpose());
			cell = row.createCell(3);
			cell.setCellValue(expList.get(i).getReceiptNo());
			cell = row.createCell(4);
			cell.setCellValue(expList.get(i).getAmount());
		}
		try {
	        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date()) + sessionManager.getLoginUser().getId() + "report.xls";
		    FileOutputStream out = new FileOutputStream(new File(path + name));
		    workbook.write(out);
		    out.close();
		    System.out.println("Excel written successfully..");
		    InputStream stream = new FileInputStream(path + name);
		    System.out.println(path + name);
			file = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "report.xls");
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		    e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ioexception");
		    e.printStackTrace();
		}
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
	public String getTaskDetail() {
		return taskDetail;
	}

	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
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

	public SaleTargetDto getCurrentTarget() {
		return currentTarget;
	}

	public void setCurrentTarget(SaleTargetDto currentTarget) {
		this.currentTarget = currentTarget;
	}

	public boolean isAssignTargetMode() {
		return assignTargetMode;
	}

	public void setAssignTargetMode(boolean assignTargetMode) {
		this.assignTargetMode = assignTargetMode;
	}

	private StreamedContent file;
	private String taskDetail;

	private List<NoteDto> notes;
	private int newNotes;

	private SaleTargetDto currentTarget = new SaleTargetDto();
	private boolean assignTargetMode = false;

	public void startAssignTarget() {
		assignTargetMode = true;
		currentTarget = new SaleTargetDto();
		System.out.println("ManagerController.startAssignTarget()");
	}

	public void assignSaleTarget() throws IOException {
		if (currentTarget.getFromDate().getTime() > (new Date()).getTime() ||
				currentTarget.getToDate().getTime() <= (new Date()).getTime()) {
			sessionManager.addGlobalMessageFatal("Time period must cover today", null);
			return;
		}
		if (currentTarget.getFromDate().getTime() > currentTarget.getToDate().getTime()) {
			sessionManager.addGlobalMessageFatal("Start date must not be later than end date", null);
			return;
		}
		if (currentTarget.getTarget() <= 0) {
			sessionManager.addGlobalMessageFatal("Invalid target amount.", null);
			return;
		}

		List<UserDto> sales = userService.getUserByTeam(team);
		int noOfEmp = sales.size();
		int amount = currentTarget.getTarget() / noOfEmp;
		for (UserDto dto: sales) {
			SaleTargetDto std = new SaleTargetDto(dto, currentTarget.getAction(), amount, currentTarget.getFromDate(), currentTarget.getToDate(), 0, currentTarget.getUnit());
			stService.addSaleTarget(std);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fromDateStr = sdf.format(currentTarget.getFromDate());
			String toDateStr = sdf.format(currentTarget.getToDate());
			String messageContent = "New sale target has been assigned: " + currentTarget.getAction().toLowerCase() + " " + amount + " " + currentTarget.getUnit() + " from " + fromDateStr + " to " + toDateStr;

			NoteDto message = new NoteDto(noteService.getSeq(), sessionManager.getLoginUser(), dto, messageContent, new Date());
			noteService.addNote(message);
		}
		assignTargetMode = false;
		sessionManager.addGlobalMessageInfo("Sale target assigned", null);
	}

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

	public void startRespond(NoteDto note) {
		setRespondMode(true);
		setTargetUser(note.getFromUser());
		setSelectedNote(note);
	}

	public boolean isRespondMode() {
		return respondMode;
	}

	public void setRespondMode(boolean respondMode) {
		this.respondMode = respondMode;
	}

	public UserDto getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(UserDto targetUser) {
		this.targetUser = targetUser;
	}

	public NoteDto getSelectedNote() {
		return selectedNote;
	}

	public void setSelectedNote(NoteDto selectedNote) {
		this.selectedNote = selectedNote;
	}

	public List<ProductDto> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<ProductDto> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public boolean isAddPromoMode() {
		return addPromoMode;
	}

	public void setAddPromoMode(boolean addPromoMode) {
		this.addPromoMode = addPromoMode;
	}

	public String getProdSearch() {
		return prodSearch;
	}

	public void setProdSearch(String prodSearch) {
		this.prodSearch = prodSearch;
	}

	public PromotionDto getNewPromo() {
		if (newPromo == null) {
			newPromo = new PromotionDto();
		}
		return newPromo;
	}

	public void setNewPromo(PromotionDto newPromo) {
		this.newPromo = newPromo;
	}

	private boolean respondMode = false;
	private UserDto targetUser;
	private NoteDto selectedNote;

	private List<ProductDto> selectedProducts;
	private boolean addPromoMode = false;
	private String prodSearch;
	private PromotionDto newPromo;

	@Inject ProductService prodService;
	@Inject PromotionService promoService;

	public void select() throws IOException {
		if (getSelectedProducts() == null) {
			setSelectedProducts(new ArrayList<ProductDto>());
		}
		int seq = Integer.parseInt(prodSearch.split("[()]")[1]);
		ProductDto prod = prodService.getProductById(seq);
		for (int i = 0; i < selectedProducts.size(); i++) {
			if (selectedProducts.get(i).getSeq() == seq)
				return;
		}
		selectedProducts.add(prod);
		prodSearch = "";
	}

	public void deleteSelected() {
		for (Iterator<ProductDto> iterator = getSelectedProducts().iterator(); iterator.hasNext();) {
		    ProductDto t = iterator.next();
		    if (t.isSelected()) {
		        iterator.remove();
		    }
		}
	}

	public List<String> suggestProd(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public void createPromo() throws IOException {
		newPromo.setProductList(selectedProducts);
		promoService.addPromotion(newPromo);
		sessionManager.addGlobalMessageInfo("New promo added", null);
		addPromoMode = false;
	}

	public void cancelCreatePromo() {
		addPromoMode = false;
		selectedProducts = new ArrayList<ProductDto>();
	}

	public void startCreatePromotion() {
		addPromoMode = true;
		selectedProducts = new ArrayList<ProductDto>();
	}

	public void cancelAssignTask() {
		assignTargetMode = false;
		currentTarget = new SaleTargetDto();
	}

	public boolean isViewActivityMode() {
		return viewActivityMode;
	}

	public void setViewActivityMode(boolean viewActivityMode) {
		this.viewActivityMode = viewActivityMode;
	}

	private boolean viewActivityMode = false;
	private List<ActivityDto> allAct = new ArrayList<ActivityDto>();

	@Inject ActivityService actService;

	public void viewActivity(UserDto sale) throws IOException {
		viewActivityMode = true;
		allAct = actService.getByUser(sale.getId());
		selectedSale = sale;
	}

	public List<ActivityDto> getAllAct() {
		return allAct;
	}

	public void setAllAct(List<ActivityDto> allAct) {
		this.allAct = allAct;
	}

	public void cancelViewActivity() {
		allAct = new ArrayList<ActivityDto>();
		viewActivityMode = false;
	}

	public boolean isAssignToOtherMode() {
		return assignToOtherMode;
	}

	public void setAssignToOtherMode(boolean assignToOtherMode) {
		this.assignToOtherMode = assignToOtherMode;
	}

	private boolean assignToOtherMode;
	private ActivityDto selectedAct;
	private String otherSalesperson;
	private List<ActivityDto> allDeal;

	@Inject ContactService contactService;

	public void startAssignToOther(ActivityDto act) {
		selectedAct = act;
		assignToOtherMode = true;
		otherSalesperson = act.getSalesperson().getName() + "(" + act.getSalesperson().getId() + ")";
	}
	public void assignToOther() throws NumberFormatException, IOException {
		if (otherSalesperson == null || otherSalesperson.isEmpty()) {
			sessionManager.addGlobalMessageInfo("Invalid info", null);
			return;
		}
		UserDto newSale = userService.findUserById(otherSalesperson.split("[()]")[1]);
		selectedAct.setSalesperson(newSale);
		assignToOtherMode = false;
		sessionManager.addGlobalMessageInfo("Deal reassigned", null);
	}
	public void cancelAssignToOther() {
		assignToOtherMode = false;
		bulkReassignMode = false;
	}

	public ActivityDto getSelectedAct() {
		return selectedAct;
	}

	public void setSelectedAct(ActivityDto selectedAct) {
		this.selectedAct = selectedAct;
	}

	public String getOtherSalesperson() {
		return otherSalesperson;
	}

	public void setOtherSalesperson(String otherSalesperson) {
		this.otherSalesperson = otherSalesperson;
	}

	public List<ActivityDto> getAllDeal() throws IOException {
		if (allDeal == null) {
			allDeal = actService.getActiveDeal();
		}
		return allDeal;
	}

	public void setAllDeal(List<ActivityDto> allDeal) {
		this.allDeal = allDeal;
	}

	public List<InvoiceDto> getEmpSaleRecords() {
		return empSaleRecords;
	}

	public void setEmpSaleRecords(List<InvoiceDto> empSaleRecords) {
		this.empSaleRecords = empSaleRecords;
	}

	public boolean isViewSaleRecordMode() {
		return viewSaleRecordMode;
	}

	public void setViewSaleRecordMode(boolean viewSaleRecordMode) {
		this.viewSaleRecordMode = viewSaleRecordMode;
	}

	private List<InvoiceDto> empSaleRecords;
	private boolean viewSaleRecordMode = false;

	@Inject InvoiceService invoiceService;
	public void startViewSaleRecord(UserDto salesperson) throws IOException {
		empSaleRecords = invoiceService.getBySalesperson(salesperson.getId());
		viewSaleRecordMode = true;
	}

	public void cancelViewSaleRecord() {
		viewSaleRecordMode = false;
	}

	public void updateUnit() {
		if (currentTarget.getAction().equalsIgnoreCase("Generate revenue of")) {
			currentTarget.setUnit("SGD");
		} else if (currentTarget.getAction().equalsIgnoreCase("Close deals with")) {
			currentTarget.setUnit("customers");
		}
	}

	public String getActivityShowingMode() {
		return activityShowingMode;
	}

	public void setActivityShowingMode(String activityShowingMode) {
		this.activityShowingMode = activityShowingMode;
	}

	private String activityShowingMode = "all";

	public void updateAllAct() throws IOException {
		if (activityShowingMode.equalsIgnoreCase("all")) {
			allAct = actService.getByUser(selectedSale.getId());
		} else if (activityShowingMode.equalsIgnoreCase("active")) {
			allAct = actService.findByStatus("Qualified", selectedSale.getId());
			allAct.addAll(actService.findByStatus("Negotiating", selectedSale.getId()));
		} else if (activityShowingMode.equalsIgnoreCase("Completed")) {
			allAct = actService.findByStatus("Completed", selectedSale.getId());
		} else if (activityShowingMode.equalsIgnoreCase("Failed")) {
			allAct = actService.findByStatus("Failed", selectedSale.getId());
		}
	}

	public void startAssignTargetToAll() {
		assignTargetMode = true;
		currentTarget = new SaleTargetDto();
	}

	public boolean isBulkReassignMode() {
		return bulkReassignMode;
	}

	public void setBulkReassignMode(boolean bulkReassignMode) {
		this.bulkReassignMode = bulkReassignMode;
	}

	public boolean isSomeDealSelected() {
		return someDealSelected;
	}

	public void setSomeDealSelected(boolean someDealSelected) {
		this.someDealSelected = someDealSelected;
	}

	private boolean someDealSelected;
	private boolean bulkReassignMode;

	public void startBulkReassign() {
		bulkReassignMode = true;
	}
	public void updateSomeDealSelected() {
		someDealSelected = false;
		for (ActivityDto dto: allDeal) {
			if (dto.isSelected()) {
				someDealSelected = true;
				break;
			}
		}
	}

	public void confirmBulkReassign() throws IOException {
		if (otherSalesperson == null || otherSalesperson.isEmpty()) {
			sessionManager.addGlobalMessageInfo("Invalid info", null);
			return;
		}
		UserDto newSale = userService.findUserById(otherSalesperson.split("[()]")[1]);
		for (ActivityDto dto: allDeal) {
			if (dto.isSelected()) {
				dto.setSalesperson(newSale);
				actService.updateActivity(dto);
			}
		}
		bulkReassignMode = false;
		sessionManager.addGlobalMessageInfo("Deals assigned", null);
	}

}
