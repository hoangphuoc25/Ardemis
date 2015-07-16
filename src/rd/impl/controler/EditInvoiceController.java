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
import javax.faces.context.ExternalContext;
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
public class EditInvoiceController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject ProductService prodService;
	@Inject CompanyService compService;
	@Inject InvoiceService invoiceService;
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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getCompSearch() throws IOException {
		if (seq != 0) {
			compSearch = getNewInvoice().getCustomer().getName() + "(" + getNewInvoice().getCustomer().getSeq() + ")";
		}
		return compSearch;
	}

	public void setCompSearch(String compSearch) {
		this.compSearch = compSearch;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<ProductDto> getSelectedProducts() throws IOException {
		if (selectedProducts == null) {
			selectedProducts = invoiceService.getProductByInvoiceId(seq);
		}
		return selectedProducts;
	}

	public void setSelectedProducts(List<ProductDto> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	private int seq;
	private String compSearch;
	private InvoiceDto newInvoice;
	private String search;
	private List<ProductDto> selectedProducts;

	public void select() throws IOException {
		if (getSelectedProducts() == null) {
			setSelectedProducts(new ArrayList<ProductDto>());
		}
		int seq = Integer.parseInt(getSearch().split("[()]")[1]);
		ProductDto prod = prodService.getProductById(seq);
		getSelectedProducts().add(prod);
	}

	public void deleteSelected() throws IOException {
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
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required", null));
		}
	}

	public void validateDate(FacesContext facesContext, UIComponent component, Object value) {
		System.out.println("validating date");
		Date d = (Date) value;
		if (d == null || d.getTime() > (new Date()).getTime())
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date", null));
	}

	public void submit() throws IOException {
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

		int customerSeq = Integer.parseInt(compSearch.split("[()]")[1]);
		System.out.println("customerseq: " +customerSeq);
		CompanyDto customer = compService.getById(customerSeq);
		newInvoice.setCustomer(customer);
		double amount = 0;
		for (ProductDto dto: selectedProducts) {
			amount += dto.getPrice();
		}
		newInvoice.setProducts(selectedProducts);
		newInvoice.setAmount(amount);
		invoiceService.updateInvoice(newInvoice);

		sessionManager.addGlobalMessageInfo("Info updated successfully", null);

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("report.jsf");
	}

	public InvoiceDto getNewInvoice() throws IOException {
		if (seq != 0) {
			newInvoice = invoiceService.getById(seq);
		}
		return newInvoice;
	}

	public void setNewInvoice(InvoiceDto newInvoice) {
		this.newInvoice = newInvoice;
	}

	public List<String> suggest(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public String productPurchased() throws IOException {
		List<ProductDto> prods = invoiceService.getProductByInvoiceId(getNewInvoice().getSeq());
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

	public void cancel() throws IOException {
		reload();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("report.jsf");
	}
}
