package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.InvoiceService;

@Named
@ConversationScoped
public class HistoryController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject SessionManager sessionManager;
	@Inject InvoiceService invoiceService;

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

	private int seq;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void submit() {
		System.out.println(seq);
	}

	private List<InvoiceDto> purchases;

	public void init() {
		Map<String, String> params =FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
		String parameterOne = params.get("seq");
		System.out.println("SEQ " + parameterOne);
		System.out.println("number:  " +seq);
		reload();
	}

	public List<InvoiceDto> getPurchases() throws IOException {
		if (purchases == null || purchases.size() == 0) {
			purchases = invoiceService.getByCustomer(seq);
		}
		return purchases;
	}

	public void setPurchases(List<InvoiceDto> purchases) {
		this.purchases = purchases;
	}





	public InvoiceDto getNewInvoice() {
		if (newInvoice == null)
			newInvoice = new InvoiceDto();
		return newInvoice;
	}

	public void setNewInvoice(InvoiceDto newInvoice) {
		this.newInvoice = newInvoice;
	}

	private InvoiceDto newInvoice;

	public List<String> suggest(String partial) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			result.add(partial + i);
		}
		return result;
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

	private String search;
	private List<ProductDto> selectedProducts;

	public void select() throws IOException {
		if (selectedProducts == null) {
			selectedProducts = new ArrayList<ProductDto>();
		}
		selectedProducts.add(new ProductDto(100, search, "abc", "abc", 100.0));
	}

	public void deleteSelected() {
		for (Iterator<ProductDto> iterator = selectedProducts.iterator(); iterator.hasNext();) {
		    ProductDto t = iterator.next();
		    if (t.isSelected()) {
		        iterator.remove();
		    }
		}
	}

	public void validateCompany(FacesContext facesContext, UIComponent component, Object value) {
		String comp = value.toString();
	}

	public void validateDate(FacesContext facesContext, UIComponent component, Object value) {
		Date d = (Date) value;

	}
}
