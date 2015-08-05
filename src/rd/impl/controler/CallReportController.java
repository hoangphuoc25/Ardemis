package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.ActivityDto;
import rd.dto.CallReportDto;
import rd.dto.CategoryDto;
import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.FaqDto;
import rd.dto.MeetingDto;
import rd.dto.ProductDto;
import rd.dto.ScheduleTaskDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ActivityService;
import rd.spec.service.CallReportService;
import rd.spec.service.CategoryService;
import rd.spec.service.CompanyService;
import rd.spec.service.ContactService;
import rd.spec.service.FaqService;
import rd.spec.service.MeetingService;
import rd.spec.service.ProductService;
import rd.spec.service.ScheduleTaskService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class CallReportController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject CompanyService compService;
	@Inject CallReportService crService;
	@Inject SessionManager sessionManager;
	@Inject ProductService prodService;
	@Inject ScheduleTaskService stService;

	@Inject MeetingService meetingService;
	@Inject FaqService faqService;

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

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public String getCallDetail() {
		return callDetail;
	}

	public void setCallDetail(String callDetail) {
		this.callDetail = callDetail;
	}

	public String getCompanyName() throws IOException {
		if ((companyName == null || companyName.isEmpty()) && custSeq != 0) {
			CompanyDto comp = compService.getById(custSeq);
			companyName = comp.getName() +"(" + comp.getSeq() + ")";
		}
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	private Date callTime = new Date();
	private String callDetail;
	private String companyName;
	private String rating;

	private List<String> ratings;

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	@Inject ActivityService actService;

	public void addNewReport() throws IOException {
		if (rating.equalsIgnoreCase("follow-up call") || rating.equalsIgnoreCase("call again later")) {
			if (callBackAgainTime != null && (callBackAgainTime.getTime() < (new Date()).getTime())) {
				sessionManager.addGlobalMessageFatal("Invalid call back time", null);
				return;
			}
			if (callBackNo < 0) {
				sessionManager.addGlobalMessageFatal("Invalid call back time", null);
				return;
			}
			if (callBackUnit.equalsIgnoreCase("month")) {
				callBackNo *= 30;
			}
		}

		System.out.println(callTime);
		ContactDto targetContact = contactService.getContactById(Integer.parseInt(contactName.split("[()]")[1]));
//		int compSeq = Integer.parseInt(companyName.split("[()]")[1]);
//		CompanyDto customer = compService.getById(compSeq);
//		int prodSeq = Integer.parseInt(productName.split("[()]")[1]);
//		ProductDto prod = prodService.getProductById(prodSeq);
		int seq = crService.getSeq();
		CallReportDto cr = new CallReportDto(seq, targetContact, callTime, callDetail, rating, sessionManager.getLoginUser(), callBackNo);

		crService.addReport(cr);

		if (cr.getRating().equalsIgnoreCase("contact again later") || cr.getRating().equalsIgnoreCase("follow-up call")) {
			Calendar date = new GregorianCalendar();
			date.setTime(callTime);
			date.add(Calendar.DAY_OF_YEAR, callBackNo);
			Date temp = null;
			if (callBackAgainTime != null) {
				temp = callBackAgainTime;
			} else {
				temp = date.getTime();
			}
			String category = "";
			if (cr.getRating().equalsIgnoreCase("contact again later"))
				category = "Call back again";
			else if (cr.getRating().equalsIgnoreCase("Follow-up"))
				category = "Follow-up call";

			int stSeq = stService.getSeq();
			ScheduleTaskDto task = new ScheduleTaskDto(stSeq, category, temp, sessionManager.getLoginUser().getId(), callDetail, targetContact);
			stService.addEvent(task);
		}

		if (cr.getRating().equalsIgnoreCase("follow-up meeting")) {
			if (meetingLocationMode.equalsIgnoreCase("true")) {
				newMeeting.setLocation(targetContact.getAddress());
			}
			newMeeting.setContact(targetContact);
			newMeeting.setSalesperson(sessionManager.getLoginUser());
			meetingService.addMeeting(newMeeting);
			sessionManager.addGlobalMessageInfo("New meeting added", null);

			int actSeq = actService.getSeq();
			ActivityDto act = new ActivityDto(actSeq, targetContact, new Date(), "Contacted", "", sessionManager.getLoginUser(), selectedProdList);
			actService.addActivity(act);
			sessionManager.addGlobalMessageInfo("New deal activity added", null);
		}

		if (targetContact.getContactStatus().equalsIgnoreCase("new")) {
			targetContact.setContactStatus("contacted");
			contactService.updateContact(targetContact);
		}

		callTime = new Date();
		callDetail = "";
		this.rating = "";
		this.companyName = "";

		callBackNo = 0;
		callBackUnit = "day";
		callBackAgainTime = null;
		// prod = new ProductDto();

		sessionManager.addGlobalMessageInfo("New call report added", null);


		if (isBack()) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("contactList.jsf?status=new");
		}

		if (isBackMain()) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(sessionController.dashboardLink());
		}
	}
	@Inject SessionController sessionController;

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public List<String> getRatings() {
		if (ratings == null || ratings.size() == 0) {
			ratings = new ArrayList<String>();
			ratings.add("Select");
			ratings.add("Follow-up call");
			ratings.add("Follow-up meeting");
			ratings.add("Contact again later");
			ratings.add("Not interested");
			ratings.add("Unresponsive");
		}
		return ratings;
	}

	public void setRatings(List<String> ratings) {
		this.ratings = ratings;
	}

	public List<CallReportDto> getAllCalls() throws IOException {
		if (allCalls == null || allCalls.size() == 0) {
			allCalls = crService.getAll();
		}
		return allCalls;
	}

	public void setAllCalls(List<CallReportDto> allCalls) {
		this.allCalls = allCalls;
	}

	private List<CallReportDto> allCalls;


	public void startAdd() {
		addMode = true;
	}

	public void addNewCompany() throws IOException {
		getNewCust().setSeq(compService.getSeq());
		compService.insertCompany(getNewCust());
		setAddMode(false);

		sessionManager.addGlobalMessageInfo("New company added", null);
		companyName = newCust.getName() + "(" + newCust.getSeq() + ")";
		newCust = new CompanyDto();
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	public CompanyDto getNewCust() {
		if (newCust == null) {
			newCust = new CompanyDto();
		}
		return newCust;
	}

	public void setNewCust(CompanyDto newCust) {
		this.newCust = newCust;
	}

	private boolean addMode = false;
	private CompanyDto newCust;

	public void validateCompName(FacesContext facesContext, UIComponent component, Object value) throws IOException {
		String name = value.toString().trim();
		List<CompanyDto> comp = compService.searchCompanyByName(name);
		if (comp != null && comp.size() > 0) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "A company with this name already exists.", null));
		}
	}

	public void cancel() {
		addMode = false;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	private String productName;

	public List<String> suggestProd(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<ProductDto> temp = prodService.searchByName(partial);
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public List<String> getCallBackUnits() {
		if (callBackUnits == null) {
			callBackUnits = new ArrayList<String>();
			callBackUnits.add("day");
			callBackUnits.add("month");
		}
		return callBackUnits;
	}

	public void setCallBackUnit(List<String> callBackUnits) {
		this.callBackUnits = callBackUnits;
	}

	public int getCallBackNo() {
		return callBackNo;
	}

	public void setCallBackNo(int callBackNo) {
		this.callBackNo = callBackNo;
	}

	public String getCallBackUnit() {
		return callBackUnit;
	}

	public void setCallBackUnit(String callBackUnit) {
		this.callBackUnit = callBackUnit;
	}

	public int getCustSeq() {
		return custSeq;
	}

	public void setCustSeq(int custSeq) {
		this.custSeq = custSeq;
	}

	public Date getCallBackAgainTime() {
		return callBackAgainTime;
	}

	public void setCallBackAgainTime(Date callBackAgainTime) {
		this.callBackAgainTime = callBackAgainTime;
	}

	public boolean isBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}

	public String getProdSearch() {
		return prodSearch;
	}

	public void setProdSearch(String prodSearch) {
		this.prodSearch = prodSearch;
	}

	public Date getScheduleSearch() {
		return scheduleSearch;
	}

	public void setScheduleSearch(Date scheduleSearch) {
		this.scheduleSearch = scheduleSearch;
	}

	public List<MeetingDto> getMySchedule() {
		return mySchedule;
	}

	public void setMySchedule(List<MeetingDto> mySchedule) {
		this.mySchedule = mySchedule;
	}

	public List<ProductDto> getSearchProdList() {
		return searchProdList;
	}

	public void setSearchProdList(List<ProductDto> searchProdList) {
		this.searchProdList = searchProdList;
	}

	private int callBackNo;
	private List<String> callBackUnits;
	private String callBackUnit;
	private int custSeq;
	private Date callBackAgainTime;
	private boolean back;


	private String prodSearch;
	private Date scheduleSearch;
	private List<MeetingDto> mySchedule  = new ArrayList<MeetingDto>();
	private List<ProductDto> searchProdList = new ArrayList<ProductDto>();

	private boolean viewFaqMode = false;
	private List<FaqDto> prodFaq;

	public List<String> suggestProduct(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public void select() throws NumberFormatException, IOException {
		ProductDto selectedProduct = prodService.getProductById(Integer.parseInt(prodSearch.split("[()]")[1]));
		searchProdList.add(selectedProduct);
	}

	public boolean isViewFaqMode() {
		return viewFaqMode;
	}

	public void setViewFaqMode(boolean viewFaqMode) {
		this.viewFaqMode = viewFaqMode;
	}

	public void startViewFaq(ProductDto prod) throws IOException {
		prodFaq = faqService.getByProduct(prod.getSeq());
		viewFaqMode = true;
	}

	public void cancelViewFaq() {
		viewFaqMode = false;
	}

	public List<FaqDto> getProdFaq() {
		return prodFaq;
	}

	public void setProdFaq(List<FaqDto> prodFaq) {
		this.prodFaq = prodFaq;
	}

	public void searchSchedule() throws IOException {
		mySchedule = meetingService.getMeetingByDayAndUser(sessionManager.getLoginUser().getId(), scheduleSearch);
	}

	public String getAssignee() throws IOException {
		if (assignee == null || assignee.isEmpty()) {
			assignee = sessionManager.getLoginUser().getName() + "(" + sessionManager.getLoginUser().getId() + ")";
		}
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	private String assignee;

	@Inject UserService userService;
	@Inject CategoryService catService;

	public List<String> suggestUser(String partial) throws IOException {
		List<UserDto> temp = userService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (UserDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getId() + ")");
		}
		return result;
	}

	private String prodDescSearch;
	private String cat;
	private List<CategoryDto> searchCatList = new ArrayList<CategoryDto>();
	private List<ProductDto> prodSearchList;

	public List<String> suggestCategory(String partial) throws IOException {
		System.out.println("ProductController.suggestCategory()");
		List<CategoryDto> temp = catService.searchCategory(partial);
		List<String> result = new ArrayList<String>();
		for (CategoryDto dto: temp) {
			result.add(dto.getCategory());
		}
		return result;
	}

	public void deleteSelectedCat() {
		System.out.println("ProductController.deleteSelectedCat()");
		for (int i = getSearchCatList().size() - 1; i >=0; i--) {
			System.out.println(getSearchCatList().get(i).getCategory() + " " + getSearchCatList().get(i).isSelected());
			if (getSearchCatList().get(i).isSelected()) {
				System.out.println(getSearchCatList().get(i).getCategory());
				getSearchCatList().remove(i);
			}
		}
	}

	public void addCat() {
		if (!searchCatList.contains(cat))
			searchCatList.add(new CategoryDto(cat));
		cat = "";
	}

	public void searchByCat() throws IOException {
		List<String> temp = new ArrayList<String>();
		for (CategoryDto cat: searchCatList) {
			temp.add(cat.getCategory());
		}
		prodSearchList = prodService.searchByCategories(temp);
		System.out.println(prodSearchList.size());
		for (ProductDto dto: prodSearchList) {
			System.out.println(dto.getName());
		}
	}

	public String getProdDescSearch() {
		return prodDescSearch;
	}

	public void setProdDescSearch(String prodDescSearch) {
		this.prodDescSearch = prodDescSearch;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public List<CategoryDto> getSearchCatList() {
		return searchCatList;
	}

	public void setSearchCatList(List<CategoryDto> searchCatList) {
		this.searchCatList = searchCatList;
	}

	public List<ProductDto> getProdSearchList() {
		return prodSearchList;
	}

	public void setProdSearchList(List<ProductDto> prodSearchList) {
		this.prodSearchList = prodSearchList;
	}

	public void searchProdByDesc() throws IOException {
		prodSearchList = prodService.searchByProductDesc(prodDescSearch);
	}

	public boolean isAddActivityMode() {
		return addActivityMode;
	}

	public void setAddActivityMode(boolean addActivityMode) {
		this.addActivityMode = addActivityMode;
	}

	public String getMeetingLocationMode() {
		if (meetingLocationMode == null || meetingLocationMode.isEmpty()) {
			meetingLocationMode = "true";
		}
		return meetingLocationMode;
	}

	public void setMeetingLocationMode(String meetingLocationMode) {
		this.meetingLocationMode = meetingLocationMode;
	}

	public String getMeetingLocation() {
		return meetingLocation;
	}

	public void setMeetingLocation(String meetingLocation) {
		this.meetingLocation = meetingLocation;
	}

	public boolean isAppendToActivity() {
		return appendToActivity;
	}

	public void setAppendToActivity(boolean appendToActivity) {
		this.appendToActivity = appendToActivity;
	}

	private boolean addActivityMode;
	private String meetingLocationMode = "true";
	private String meetingLocation;
	private String meetingPurpose;
	private boolean appendToActivity;
	private MeetingDto newMeeting = new MeetingDto();

	public void clean() {

	}

	public String getMeetingPurpose() {
		return meetingPurpose;
	}

	public void setMeetingPurpose(String meetingPurpose) {
		this.meetingPurpose = meetingPurpose;
	}

	public MeetingDto getNewMeeting() {
		return newMeeting;
	}

	public void setNewMeeting(MeetingDto newMeeting) {
		this.newMeeting = newMeeting;
	}

	public boolean isAddContactMode() {
		return addContactMode;
	}

	public void setAddContactMode(boolean addContactMode) {
		this.addContactMode = addContactMode;
	}

	private boolean addContactMode = false;
	private ContactDto newContact = new ContactDto();
	private String contactCompName;

	@Inject ContactService contactService;

	public void startAddNewContact() throws NumberFormatException, IOException {
		addContactMode = true;
	}

	public void cancelAddNewContact() {
		addContactMode = false;
		setNewContact(new ContactDto());
	}

	public void addNewContact() throws NumberFormatException, IOException {
		addContactMode = false;
		newContact.setSeq(contactService.getSeq());
		newContact.setAssignee(sessionManager.getLoginUser());
		contactService.addContact(newContact);

		contactName = newContact.getName() + " - " + newContact.getCompany() + "(" + newContact.getSeq() + ")";
		sessionManager.addGlobalMessageInfo("New contact added", null);
		setNewContact(new ContactDto());
	}

	public ContactDto getNewContact() {
		return newContact;
	}

	public void setNewContact(ContactDto newContact) {
		this.newContact = newContact;
	}

	public String getContactCompName() {
		return contactCompName;
	}

	public void setContactCompName(String contactCompName) {
		this.contactCompName = contactCompName;
	}

	public int getContactSeq() {
		return contactSeq;
	}

	public void setContactSeq(int contactSeq) {
		this.contactSeq = contactSeq;
	}

	public String getContactName() throws IOException {
		if ((contactName == null || contactName.isEmpty()) && contactSeq != 0) {
			ContactDto current = contactService.getContactById(contactSeq);
			if (current.getGender().equalsIgnoreCase("male"))
				contactName = "Mr. ";
			else
				contactName = "Ms. ";
			contactName += current.getName() + " - " + current.getCompany() + "(" + current.getSeq() + ")";
		}
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public boolean isViewDetailMode() {
		return viewDetailMode;
	}

	public void setViewDetailMode(boolean viewDetailMode) {
		this.viewDetailMode = viewDetailMode;
	}

	public ProductDto getSelectedProd() {
		return selectedProd;
	}

	public void setSelectedProd(ProductDto selectedProd) {
		this.selectedProd = selectedProd;
	}

	private int contactSeq;
	private String contactName;
	private boolean viewDetailMode;
	private ProductDto selectedProd;

	public void startViewDetail(ProductDto prod) {
		System.out.println(prod.getName());
		System.out.println(prod.getPrice());
		selectedProd = new ProductDto(prod);
		viewDetailMode = true;
	}
	public void cancelViewDetail() {
		selectedProd = new ProductDto();
		viewDetailMode = false;
	}

	public String getSearchProd() {
		return searchProd;
	}

	public void setSearchProd(String searchProd) {
		this.searchProd = searchProd;
	}

	public List<ProductDto> getSelectedProdList() {
		if (selectedProdList == null) {
			selectedProdList = new ArrayList<ProductDto>();
		}
		return selectedProdList;
	}

	public void setSelectedProdList(List<ProductDto> selectedProdList) {
		this.selectedProdList = selectedProdList;
	}

	private String searchProd;
	private List<ProductDto> selectedProdList;
	private boolean addProdMode;

	public void addProdToList() throws NumberFormatException, IOException {
		ProductDto temp = prodService.getProductById(Integer.parseInt(searchProd.split("[()]")[1]));
		searchProd = "";
		for (ProductDto pr: selectedProdList) {
			if (pr.getSeq() == temp.getSeq()) {
				return;
			}
		}
		selectedProdList.add(temp);
	}
	public void startAddProducts() {
		addProdMode = true;
//		selectedProdList = new ArrayList<ProductDto>();
	}
	public void confirmProductList() {
		addProdMode = false;
	}
	public void cancelAddProd() {
		addProdMode = false;
		selectedProdList = null;
	}

	public boolean isAddProdMode() {
		return addProdMode;
	}

	public void setAddProdMode(boolean addProdMode) {
		this.addProdMode = addProdMode;
	}

	public void deleteSelectedProduct() {
		for (int i = selectedProdList.size() - 1; i >= 0; i--) {
			if (selectedProdList.get(i).isSelected()) {
				selectedProdList.remove(i);
			}
		}
	}

	private boolean addMeetingDetailMode;

	public void startAddNewMeeting() {
		setAddMeetingDetailMode(true);
		System.out.println("CallReportController.startAddNewMeeting()");
	}
	public void cancelAddNewMeeting() {
		setAddMeetingDetailMode(false);
		System.out.println("CallReportController.cancelAddNewMeeting()");
	}

	public boolean isAddMeetingDetailMode() {
		return addMeetingDetailMode;
	}

	public void setAddMeetingDetailMode(boolean addMeetingDetailMode) {
		this.addMeetingDetailMode = addMeetingDetailMode;
	}

	public boolean isBackMain() {
		return backMain;
	}

	public void setBackMain(boolean backMain) {
		this.backMain = backMain;
	}

	private boolean backMain;
}
