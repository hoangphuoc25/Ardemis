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
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.CallReportDto;
import rd.dto.CompanyDto;
import rd.dto.ProductDto;
import rd.dto.ScheduleTaskDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CallReportService;
import rd.spec.service.CompanyService;
import rd.spec.service.ProductService;
import rd.spec.service.ScheduleTaskService;

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

	public void addNewReport() throws IOException {
		if (callBackAgainTime != null && (callBackAgainTime.getTime() < (new Date()).getTime())) {
			sessionManager.addGlobalMessageFatal("Invalid call back time", null);
			return;
		}
		if (callBackNo < 0) {
			sessionManager.addGlobalMessageFatal("Invalid call back time", null);
			return;
		}

		if (productName.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Product name can't be empty", null);
			return;
		}
		if (companyName.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Company name can't be empty", null);
			return;
		}

		if (callBackUnit.equalsIgnoreCase("month")) {
			callBackNo *= 30;
		}
		System.out.println(callTime);
		int compSeq = Integer.parseInt(companyName.split("[()]")[1]);
		CompanyDto customer = compService.getById(compSeq);
		int prodSeq = Integer.parseInt(productName.split("[()]")[1]);
		ProductDto prod = prodService.getProductById(prodSeq);
		int seq = crService.getSeq();
		CallReportDto cr = new CallReportDto(seq, customer, callTime, callDetail, rating, sessionManager.getLoginUser(), prod, callBackNo);

		crService.addReport(cr);

		if (cr.getRating().equalsIgnoreCase("contact again later") || cr.getRating().equalsIgnoreCase("follow-up")) {
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
			ScheduleTaskDto task = new ScheduleTaskDto(stSeq, category, customer, temp, sessionManager.getLoginUser().getId(), callDetail);
			stService.addEvent(task);
		}

		if (customer.getContactStatus().equalsIgnoreCase("new")) {
			customer.setContactStatus("contacted");
			compService.updateCompany(customer);
		}

		callTime = new Date();
		callDetail = "";
		this.rating = "";
		this.companyName = "";

		callBackNo = 0;
		callBackUnit = "day";
		callBackAgainTime = null;
		prod = new ProductDto();

		sessionManager.addGlobalMessageInfo("New call report added", null);
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public List<String> getRatings() {
		if (ratings == null || ratings.size() == 0) {
			ratings = new ArrayList<String>();
			ratings.add("Follow-up");
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

	private int callBackNo;
	private List<String> callBackUnits;
	private String callBackUnit;
	private int custSeq;
	private Date callBackAgainTime;
}
