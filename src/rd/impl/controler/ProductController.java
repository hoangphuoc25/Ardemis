package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
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

import rd.dto.ProductDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ProductService;

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
		reload();
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

	public void showDialog() {
		reload();
		addMode = false;
		editMode = false;
		viewMode = true;
		newProd = new ProductDto(selectedProd);
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public void startEdit() {
		reload();
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
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("prodDialog_w.hide();");
		}
	}

	public void delete() throws IOException {
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
		System.out.println("ProductController.getSeq()");
		System.out.println(seq);
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	private int seq;

	@Inject SessionController sessionController;
	public double deducePrice(ProductDto prod) {
		if (sessionController.getCurrency() == 0)
			return prod.getPrice();
		return prod.getPrice()*sessionController.getRates().get(sessionController.getCurrency());
	}
}
