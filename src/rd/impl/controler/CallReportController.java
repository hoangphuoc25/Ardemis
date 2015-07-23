package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import rd.spec.manager.SessionManager;
import rd.spec.service.CallReportService;
import rd.spec.service.CompanyService;

@Named
@ConversationScoped
public class CallReportController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject CompanyService compService;
	@Inject CallReportService crService;
	@Inject SessionManager sessionManager;

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

	public String getCompanyName() {
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

		System.out.println(callTime);
		int compSeq = Integer.parseInt(companyName.split("[()]")[1]);
		CompanyDto customer = compService.getById(compSeq);
		int seq = crService.getSeq();
		CallReportDto cr = new CallReportDto(seq, customer, callTime, callDetail, rating, sessionManager.getLoginUser());

		crService.addReport(cr);

		callTime = null;
		callDetail = "";
		this.rating = "";
		this.companyName = "";

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
			ratings.add("POSITIVE");
			ratings.add("NEUTRAL");
			ratings.add("NEGATIVE");
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
}
