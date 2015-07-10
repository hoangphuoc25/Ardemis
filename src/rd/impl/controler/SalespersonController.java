package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CompanyDto;
import rd.dto.NoteDto;
import rd.dto.TeamDto;
import rd.dto.UserDto;
import rd.spec.service.CompanyService;
import rd.spec.service.TeamService;
import rd.utils.DatabaseUtil;

@Named
@ConversationScoped
public class SalespersonController implements Serializable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	@Inject DatabaseUtil dbCache;
	@Inject TeamService teamService;
	@Inject Conversation conversation;
	@Inject CompanyService comService;

	private List<TeamDto> teams;
	private List<CompanyDto> customerList;

	private CompanyDto newCust = new CompanyDto();

	public List<TeamDto> getTeams() throws IOException {
		if (teams == null || teams.size() == 0) {
			teams = teamService.getAll();
		}
		return teams;
	}

	public void setTeams(List<TeamDto> teams) {
		this.teams = teams;
	}

	public void reload() {
		conversationBegin();
	}

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

	public void abc() {
		reload();
		teams.remove(3);
	}

	public List<CompanyDto> getCustomerList() throws IOException {
		if (customerList == null) {
			customerList = comService.getAll();
		}
		return customerList;
	}

	public void setCustomerList(List<CompanyDto> customerList) {
		this.customerList = customerList;
	}

	public void searchCustomer(String partialName) throws IOException {
		customerList = comService.searchCompanyByName(partialName);
	}

	public CompanyDto getNewCust() {
		return newCust;
	}

	public void setNewCust(CompanyDto newCust) {
		this.newCust = newCust;
	}

	public void addNewCompany() throws IOException {
		logger.error("ADDNEWCOMPANY ENTRY");
		newCust.setSeq(comService.getSeq());
		comService.insertCompany(newCust);
		customerList.add(newCust);
		newCust = new CompanyDto();
		logger.error("ADDNEWCOMPANY EXIT");
		addMode = false;
		conversationEnd();
	}

	public void startAdd() {
		reload();
		logger.error("COMPLETED");
		addMode = true;
	}

	public void search(String a) throws IOException {
		logger.error(a);
		searchCustomer(a);
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	public CompanyDto getSelectedCom() {
		return selectedCom;
	}

	public void setSelectedCom(CompanyDto selectedCom) {
		this.selectedCom = selectedCom;
	}

	private boolean addMode = false;
	private boolean editMode;
	private CompanyDto selectedCom;


	public String addNew() throws IOException {
		newCust.setSeq(comService.getSeq());
		comService.insertCompany(newCust);
		newCust = new CompanyDto();
		return "/salesperson.jsf";
	}

	public void discard() {
		newCust = null;
		addMode = false;
	}

	public void addNewCompany2() throws IOException {
		newCust.setSeq(comService.getSeq());
		comService.insertCompany(newCust);
		customerList.add(newCust);
		newCust = new CompanyDto();
		addMode = false;

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("custDialog_w.close()");

		conversationEnd();
	}

	public String link(String name) {
		return "https://www.google.com.sg/?gws_rd=ssl#q="+name+"&tbm=nws";
	}
}

