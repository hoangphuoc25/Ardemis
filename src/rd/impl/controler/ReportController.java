package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CompanyService;
import rd.spec.service.InvoiceService;
import rd.spec.service.ProductService;

@Named
@ConversationScoped
public class ReportController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject InvoiceService invoiceService;
	@Inject SessionManager sessionManager;
	@Inject CompanyService compService;
	@Inject ProductService prodService;

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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<InvoiceDto> getInvoices() throws IOException {
		if (invoices == null || invoices.size() == 0) {
			invoices = invoiceService.getAll();
		}
		return invoices;
	}

	public void setInvoices(List<InvoiceDto> invoices) {
		this.invoices = invoices;
	}

	private Date fromDate;
	private Date toDate;
	private List<InvoiceDto> invoices;

	public void searchBefore() throws IOException {
		invoices = invoiceService.searchInvoiceBeforeDate(toDate);
	}

	public void searchAfter() throws IOException {
		invoices = invoiceService.searchInvoiceAfterDate(fromDate);
	}

	public void searchBeforeAndAfter() throws IOException {
		invoices = invoiceService.searchInvoiceBeforeAfter(fromDate, toDate);
		System.out.println(invoices.size());
	}

	public void search() throws IOException {
		reload();
		if (fromDate == null && toDate != null) {
			searchBefore();
		} else if (fromDate != null && toDate == null) {
			searchAfter();
		} else if (fromDate != null && toDate != null) {
			searchBeforeAndAfter();
		}
	}

	@Inject SessionController sessionController;
	public double deduceAmount(InvoiceDto invoice) {
		if (sessionController.getCurrency() == 0)
			return invoice.getAmount();
		return invoice.getAmount()*sessionController.getRates().get(sessionController.getCurrency());
	}

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

	public List<String> compSuggest(String partial) throws IOException {
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		List<String> result = new ArrayList<String>();
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	private String compSearch;
	private InvoiceDto newInvoice;
	private String search;
	private List<ProductDto> selectedProducts;

	public void select() throws IOException {
		if (getSelectedProducts() == null) {
			setSelectedProducts(new ArrayList<ProductDto>());
		}
		// selectedProducts.add(new ProductDto(100, search, "abc", "abc", 100.0));
		int seq = Integer.parseInt(getSearch().split("[()]")[1]);
		ProductDto prod = prodService.getProductById(seq);
		for (int i = 0; i < selectedProducts.size(); i++) {
			if (selectedProducts.get(i).getSeq() == seq)
				return;
		}
		selectedProducts.add(prod);
	}

	public void deleteSelected() {
		for (Iterator<ProductDto> iterator = getSelectedProducts().iterator(); iterator.hasNext();) {
		    ProductDto t = iterator.next();
		    if (t.isSelected()) {
		        iterator.remove();
		    }
		}
	}

	public void validateCompany(FacesContext facesContext, UIComponent component, Object value) {
		System.out.println("ReportController.validateCompany()");
		String comp = value.toString();
		if (comp.isEmpty()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Company name is required", null));
		}
	}

	public void validateDate(FacesContext facesContext, UIComponent component, Object value) {
		System.out.println("validating date");
		Date d = (Date) value;
		if (d == null || d.getTime() > (new Date()).getTime())
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date", null));
	}

	public void addNewInvoice() throws IOException {
		if (selectedProducts == null || selectedProducts.size() == 0) {
			sessionManager.addGlobalMessageFatal("Product list empty", null);
			return;
		}
		if (newInvoice.getPurchaseDate() == null) {
			sessionManager.addGlobalMessageFatal("Invalid date", null);
			return;
		}
		if (compSearch == null || compSearch.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Company name is required", null);
			return;
		}
		getNewInvoice().setSeq(invoiceService.getSeq());
		int customerSeq = Integer.parseInt(getCompSearch().split("[()]")[1]);
		CompanyDto customer = compService.getById(customerSeq);
		getNewInvoice().setCustomer(customer);
		double amount = 0;
		for (ProductDto dto: getSelectedProducts()) {
			amount += dto.getPrice();
		}
		getNewInvoice().setProducts(getSelectedProducts());
		getNewInvoice().setAmount(amount);
		newInvoice.setSalesperson(sessionManager.getLoginUser());
		invoiceService.addInvoice(getNewInvoice());

		invoices.add(newInvoice);

		sessionManager.addGlobalMessageInfo("New purchase record added", null);
		setCompSearch("");
		setSearch("");
		setSelectedProducts(new ArrayList<ProductDto>());
		newInvoice = new InvoiceDto();
	}

	public String getCompSearch() {
		return compSearch;
	}

	public void setCompSearch(String compSearch) {
		this.compSearch = compSearch;
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

	public List<ProductDto> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<ProductDto> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public List<String> suggest(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public void clear() throws IOException {
		fromDate = null;
		toDate = null;
		invoices = invoiceService.getAll();
	}

	public void remove(InvoiceDto invoice) throws IOException {
		for (int i = invoices.size() - 1; i >= 0; i--) {
			if (invoices.get(i).getSeq() == invoice.getSeq()) {
				invoiceService.deleteById(invoice.getSeq());
				invoices.remove(i);
				return;
			}
		}
	}

	public String getCompanySearch() {
		return companySearch;
	}

	public void setCompanySearch(String companySearch) {
		this.companySearch = companySearch;
	}

	public String getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(String customerSearch) {
		this.customerSearch = customerSearch;
	}

	private String customerSearch;
	private String companySearch;

	public void searchByCustomerName() {

	}

	public void searchByCompanyName() {

	}
}
