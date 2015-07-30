package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
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

import org.primefaces.context.RequestContext;

import rd.dto.FeedbackDto;
import rd.dto.InvoiceDto;
import rd.dto.NoteDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.FeedbackService;
import rd.spec.service.InvoiceService;
import rd.spec.service.NoteService;
import rd.spec.service.ProductService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class ProductController implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject ProductService productService;
	@Inject SessionManager sessionManager;
	@Inject InvoiceService invoiceService;
	@Inject SessionController sessionController;
	@Inject FeedbackService fbService;
	@Inject UserService userService;
	@Inject NoteService noteService;

	private ProductDto newProd = new ProductDto();
	private List<ProductDto> products;
	private String searchStr;
	private boolean addMode = false;
	private boolean editMode = false;
	private boolean viewMode = false;

	public ProductDto getNewProd() {
		return newProd;
	}

	public void setNewProd(ProductDto newProd) {
		this.newProd = newProd;
	}

	public List<ProductDto> getProducts() throws IOException {
		if (products == null || products.size() == 0) {
			products = productService.getAll();
		}
		return products;
	}

	public void setProducts(List<ProductDto> products) {
		this.products = products;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	public void startAdd() {
		// reload();
		newProd = new ProductDto();
		addMode = true;
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

	public void reload() {
		conversationBegin();
	}

	public void validateName(FacesContext context, UIComponent component, Object value) throws IOException {
		String name = value.toString().trim();
		if (name.isEmpty()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a name", null));
		}
		if (editMode) {
			return;
		}
		ProductDto search = productService.getByName(name);
		if (search != null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "A product with this name already exists", null));
		}
    }

	public void addProduct() throws IOException {
		int seq = productService.getSeq();
		newProd.setName(newProd.getName().trim());
		newProd.setSeq(seq);
		productService.addProduct(newProd);
		products.add(newProd);
		addMode = false;

		if (notifyOther) {
			List<UserDto> saleList = userService.getUserByRole("sale");
			for (UserDto sale: saleList) {
				String noteContent = "A new product has been added: " + newProd.getName();
				int noteSeq = noteService.getSeq();
				NoteDto note = new NoteDto(noteSeq, sessionManager.getLoginUser(), sale, noteContent, new Date());
				noteService.addNote(note);
			}
		}
		sessionManager.addGlobalMessageInfo("New product added", null);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("prodDialog_w.hide();");
	}

	public void search() throws IOException {
		products = productService.searchByName(searchStr);
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public void showDialog(ProductDto prod) {
		// reload();
		addMode = false;
		editMode = false;
		viewMode = true;
		selectedProd = new ProductDto(prod);
		newProd = new ProductDto(selectedProd);
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public void startEdit() {
		// reload();
		addMode = false;
		editMode = true;
		viewMode = false;
		System.out.println("ProductController.startEdit()");
	}

	public ProductDto getSelectedProd() {
		return selectedProd;
	}

	public void setSelectedProd(ProductDto selectedProd) {
		this.selectedProd = selectedProd;
	}

	private ProductDto selectedProd;

	public void apply() throws IOException {
		if (addMode) {
			addProduct();
		} else {
			addMode = false;
			editMode = false;
			viewMode = false;
			sessionManager.addGlobalMessageInfo("Info updated successfully", null);
			productService.updateProduct(newProd);
			for (int i = 0; i < products.size(); i++) {
				if (products.get(i).getSeq() == newProd.getSeq()) {
					products.set(i, newProd);
					break;
				}
			}
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("prodDialog_w.hide();");
		}
	}

	public void delete() throws IOException {
		List<InvoiceDto> invoices = invoiceService.findInvoicesByProduct(seq);
		if (invoices != null && invoices.size() > 0) {
			sessionManager.addGlobalMessageFatal("Can't delete product", null);
			return;
		}

		productService.removeProduct(newProd.getSeq());
		for (ProductDto p: products) {
			if (p.getSeq() == newProd.getSeq()) {
				products.remove(p);
				break;
			}
		}

		addMode = false;
		editMode = false;
		viewMode = false;
		sessionManager.addGlobalMessageInfo("Product removed successully.", null);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("prodDialog_w.hide();");
	}

	public void cancel() {
		addMode = false;
		editMode = false;
		viewMode = false;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("prodDialog_w.hide();");
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	private int seq;

	public double deducePrice(ProductDto prod) {
		if (sessionController.getCurrency() == 0)
			return prod.getPrice();
		return prod.getPrice()*sessionController.getRates().get(sessionController.getCurrency());
	}

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}

	public List<FeedbackDto> getFeedbacks() throws IOException {
		if (feedbacks == null || feedbacks.size() == 0) {
			feedbacks = fbService.getFeedbackByProduct(seq);
		}
		return feedbacks;
	}

	public void setFeedbacks(List<FeedbackDto> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public List<InvoiceDto> getPurchases() throws IOException {
		if (purchases == null || purchases.size() == 0) {
			purchases = invoiceService.findInvoicesByProduct(seq);
		}
		return purchases;
	}

	public void setPurchases(List<InvoiceDto> purchases) {
		this.purchases = purchases;
	}

	public boolean isViewDetailMode() {
		return viewDetailMode;
	}

	public void setViewDetailMode(boolean viewDetailMode) {
		this.viewDetailMode = viewDetailMode;
	}

	private List<InvoiceDto> purchases;
	private List<FeedbackDto> feedbacks;
	private boolean viewDetailMode = false;

	public void startShowDetail(ProductDto prod) throws IOException {
		// reload();
		viewDetailMode = true;
		feedbacks = fbService.getFeedbackByProduct(prod.getSeq());
		purchases = invoiceService.findInvoicesByProduct(prod.getSeq());
	}

	public double deduceAmount(InvoiceDto invoice) {
		if (sessionController.getCurrency() == 0)
			return invoice.getAmount();
		return invoice.getAmount()*sessionController.getRates().get(sessionController.getCurrency());
	}

	public void detailCancel() {
		viewDetailMode = false;
	}

	public void startShowCatalog(ProductDto prod) {
		showCatalogMode = true;
	}

	public boolean isShowCatalogMode() {
		return showCatalogMode;
	}

	public void setShowCatalogMode(boolean showCatalogMode) {
		this.showCatalogMode = showCatalogMode;
	}

	public String getDetailPath() {
		detailPath = "../products/AutoCAD.xhtml";
		return detailPath;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
	}

	private boolean showCatalogMode = false;
	private String detailPath;

	public String catalogLink(ProductDto prod) {
		if (prod.getName().equalsIgnoreCase("autocad")) {
			return "../products/AutoCAD.jsf";
		} else if (prod.getName().equalsIgnoreCase("revit")) {
			return "../products/Revit.jsf";
		} else if (prod.getName().equalsIgnoreCase("quickdesk")) {
			return "../products/quickdesk.jsf";
		} else {
			return "../products/Stormworks.jsf";
		}
	}

	public boolean isNotifyOther() {
		return notifyOther;
	}

	public void setNotifyOther(boolean notifyOther) {
		this.notifyOther = notifyOther;
	}

	private boolean notifyOther;

	public String noteLink(NoteDto note) throws IOException {
		if (note.getNote().startsWith("You have been assigned this company:")) {
			String comSeq = note.getNote().split("[()]")[1];
			return "../faces/history.jsf?seq="+comSeq;
		} else if (note.getNote().startsWith("A new product has been added:")) {
			String productName = note.getNote().split(":")[1].trim();
			ProductDto prod = productService.getByName(productName);
			return "../faces/productdetail.jsf?seq="+prod.getSeq();
		} else {
			return "";
		}
	}
}
