package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CallReportDto;
import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.dto.TeamDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CallReportService;
import rd.spec.service.CompanyService;
import rd.spec.service.InvoiceService;
import rd.spec.service.ProductService;
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
	@Inject SessionManager sessionManager;
	@Inject InvoiceService invoiceService;
	@Inject ProductService prodService;

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
		if (customerList == null && (status == null || status.isEmpty())) {
			customerList = comService.getAll();
		} else if (customerList == null && !status.isEmpty()) {
			customerList = comService.getCompanyByContactStatus(status);
			showingMode = status;
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
		newCust.setSeq(comService.getSeq());
		comService.insertCompany(newCust);
		getCustomerList().add(newCust);
		newCust = new CompanyDto();
		addMode = false;

		sessionManager.addGlobalMessageInfo("New company added", null);
		conversationEnd();
	}

	public void startAdd() {
		// reload();
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

	public void cancel() {
		addMode = false;
		editMode = false;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("custDialog_w.hide();");
	}

	public void validateCompName(FacesContext facesContext, UIComponent component, Object value) throws IOException {
		String name = value.toString().trim();
		CompanyDto comp = comService.searchCompanyByNameExact(name);
		if (comp != null)
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "A company with this name already exists.", null));
	}

	public void remove(CompanyDto comp) throws IOException {
		List<InvoiceDto> temp = invoiceService.getByCustomer(comp.getSeq());
		if (temp != null && temp.size() > 0) {
			sessionManager.addGlobalMessageFatal("Can't delete customer.", null);
			return;
		}
		comService.deleteCompany(comp.getSeq());
		for (int i = customerList.size() -1; i >= 0; i--) {
			if (customerList.get(i).getSeq() == comp.getSeq()) {
				customerList.remove(i);
				return;
			}
		}
	}

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}

	public String getShowingMode() {
		return showingMode;
	}

	public void setShowingMode(String showingMode) {
		this.showingMode = showingMode;
	}

	private String showingMode = "all";

	public void updateCompanyList() throws IOException {
		if (showingMode.equals("all"))
			customerList = comService.getAll();
		else
			customerList = comService.getCompanyByContactStatus(showingMode);
		purchasedProduct = "";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPurchasedProduct() {
		return purchasedProduct;
	}

	public void setPurchasedProduct(String purchasedProduct) {
		this.purchasedProduct = purchasedProduct;
	}

	private String status;

	private String purchasedProduct;

	public List<String> suggestProductList(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+ dto.getSeq() + ")");
		}
		return result;
	}

	public void searchCompanyByProduct() throws NumberFormatException, IOException {
		if (purchasedProduct.isEmpty())
			customerList = comService.getAll();
		else
			customerList = invoiceService.findCompanyByProduct(Integer.parseInt(purchasedProduct.split("[()]")[1]));
		showingMode = "all";
		purchasedProduct = "";
	}

	public void searchIndustry(String industry) throws IOException {
		customerList = comService.searchByIndustry(industry);
	}

	public void searchLocation(String location) throws IOException {
		customerList = comService.searchByLocation(location);
	}

	public void clearSearch() throws IOException {
		customerList = comService.getAll();
	}

	public boolean isViewDetailMode() {
		return viewDetailMode;
	}

	public void setViewDetailMode(boolean viewDetailMode) {
		this.viewDetailMode = viewDetailMode;
	}

	public CompanyDto getSelectedComp() {
		if (selectedComp == null) {
			selectedComp = new CompanyDto();
		}
		return selectedComp;
	}

	public void setSelectedComp(CompanyDto selectedComp) {
		this.selectedComp = selectedComp;
	}

	public List<CallReportDto> getCallResults() {
		return callResults;
	}

	public void setCallResults(List<CallReportDto> callResults) {
		this.callResults = callResults;
	}

	public List<InvoiceDto> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<InvoiceDto> purchases) {
		this.purchases = purchases;
	}

	private boolean viewDetailMode = false;
	private CompanyDto selectedComp;
	private List<CallReportDto> callResults;
	private List<InvoiceDto> purchases;

	@Inject CallReportService crService;

	public String productPurchased(InvoiceDto invoice) throws IOException {
		List<ProductDto> prods = invoiceService.getProductByInvoiceId(invoice.getSeq());
		String result = "";
		for (ProductDto dto: prods) {
			result += dto.getName() + ", ";
		}
		if (result.length() > 2)
			result = result.substring(0, result.length() - 2);
		return result;
	}

	public void startViewingComp(CompanyDto comp) throws IOException {
		callResults = crService.getByCompanyId(comp.getSeq());
		purchases = invoiceService.getByCustomer(comp.getSeq());
		viewDetailMode = true;
		selectedComp = comp;
	}

	public void cancelViewDetailMode() {
		viewDetailMode = false;
		callResults = new ArrayList<CallReportDto>();
		purchases = new ArrayList<InvoiceDto>();
	}
}

