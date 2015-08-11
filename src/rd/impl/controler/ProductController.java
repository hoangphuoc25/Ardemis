package rd.impl.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import rd.dto.CategoryDto;
import rd.dto.ContactDto;
import rd.dto.FaqDto;
import rd.dto.FeedbackDto;
import rd.dto.InvoiceDto;
import rd.dto.NoteDto;
import rd.dto.ProductDto;
import rd.dto.PromotionDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CategoryService;
import rd.spec.service.FaqService;
import rd.spec.service.FeedbackService;
import rd.spec.service.InvoiceService;
import rd.spec.service.NoteService;
import rd.spec.service.ProductService;
import rd.spec.service.PromotionService;
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
		if (products == null) {
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

	public void startAdd() throws IOException {
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
		newProd.setCategory(catList);
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
		catList = new ArrayList<CategoryDto>();

//		RequestContext context = RequestContext.getCurrentInstance();
//		context.execute("prodDialog_w.hide();");
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

	public void showDialog(ProductDto prod) throws IOException {
		// reload();
		addMode = false;
		editMode = false;
		viewMode = true;
		selectedProd = new ProductDto(prod);
		List<CategoryDto> prodCat = catService.getCategoryByProduct(prod.getSeq());
		newProd = new ProductDto(selectedProd);
		newProd.setCategory(prodCat);
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
			newProd.setCategory(catList);

			productService.updateProduct(newProd);
			for (int i = 0; i < products.size(); i++) {
				if (products.get(i).getSeq() == newProd.getSeq()) {
					products.set(i, newProd);
					break;
				}
			}

			sessionManager.addGlobalMessageInfo("Info updated successfully", null);
			catList = new ArrayList<CategoryDto>();
//			RequestContext context = RequestContext.getCurrentInstance();
//			context.execute("prodDialog_w.hide();");
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
//		RequestContext context = RequestContext.getCurrentInstance();
//		context.execute("prodDialog_w.hide();");
	}

	public void cancel() {
		addMode = false;
		editMode = false;
		viewMode = false;
//		RequestContext context = RequestContext.getCurrentInstance();
//		context.execute("prodDialog_w.hide();");
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
		return "../main.jsf?faces-redirect=true";
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

	private List<ProductDto> selectedProducts;
	private boolean addPromoMode = false;
	private String prodSearch;
	private PromotionDto newPromo;

	@Inject ProductService prodService;
	@Inject PromotionService promoService;

	public void select() throws IOException {
		if (getSelectedProducts() == null) {
			setSelectedProducts(new ArrayList<ProductDto>());
		}
		int seq = Integer.parseInt(getProdSearch().split("[()]")[1]);
		ProductDto prod = prodService.getProductById(seq);
		for (int i = 0; i < getSelectedProducts().size(); i++) {
			if (getSelectedProducts().get(i).getSeq() == seq)
				return;
		}
		getSelectedProducts().add(prod);
		setProdSearch("");
	}

	public void deleteSelected() {
		for (Iterator<ProductDto> iterator = getSelectedProducts().iterator(); iterator.hasNext();) {
		    ProductDto t = iterator.next();
		    if (t.isSelected()) {
		        iterator.remove();
		    }
		}
	}

	public List<String> suggestProd(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public void createPromo() throws IOException {
		getNewPromo().setProductList(getSelectedProducts());
		promoService.addPromotion(getNewPromo());
		sessionManager.addGlobalMessageInfo("New promo added", null);
		setAddPromoMode(false);
	}

	public void cancelCreatePromo() {
		setAddPromoMode(false);
		setSelectedProducts(new ArrayList<ProductDto>());
	}

	public void startCreatePromotion() {
		setAddPromoMode(true);
		setSelectedProducts(new ArrayList<ProductDto>());
	}

	public boolean isAddPromoMode() {
		return addPromoMode;
	}

	public void setAddPromoMode(boolean addPromoMode) {
		this.addPromoMode = addPromoMode;
	}

	public List<ProductDto> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<ProductDto> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public PromotionDto getNewPromo() {
		if (newPromo == null) {
			newPromo = new PromotionDto();
		}
		return newPromo;
	}

	public void setNewPromo(PromotionDto newPromo) {
		this.newPromo = newPromo;
	}

	public String getProdSearch() {
		return prodSearch;
	}

	public void setProdSearch(String prodSearch) {
		this.prodSearch = prodSearch;
	}




	public List<PromotionDto> getAllPromo() throws IOException {
		if (allPromo == null) {
			allPromo = promoService.getActive();
			for (PromotionDto dto: allPromo) {
				dto.setProductList(promoService.getProductList(dto.getSeq()));
			}
		}
		return allPromo;
	}

	public void setAllPromo(List<PromotionDto> allPromo) {
		this.allPromo = allPromo;
	}

	private List<PromotionDto> allPromo;

	public String getProductList(PromotionDto promo) {
		String result = "";
		for (ProductDto dto: promo.getProductList()) {
			result += dto.getName() + ", ";
		}
		if (result.length() >= 2) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}

	public double calcPrice(PromotionDto promo) {
		double price = 0;
		for (ProductDto dto: promo.getProductList()) {
			price += dto.getPrice();
		}
		price = price * (1 - (double)promo.getDiscount()/(double)100);
		price = price * sessionController.getRates().get(sessionController.getCurrency());
		return price;
	}

	public String getPromoProdSearch() {
		return promoProdSearch;
	}

	public void setPromoProdSearch(String promoProdSearch) {
		this.promoProdSearch = promoProdSearch;
	}

	private String promoProdSearch;

	public void searchPromoByProduct() throws IOException {
		int prodSeq = Integer.parseInt(promoProdSearch.split("[()]")[1]);
		List<PromotionDto> result = new ArrayList<PromotionDto>();
		for (PromotionDto dto: allPromo) {
			for (ProductDto prodDto: dto.getProductList()) {
				if (prodDto.getSeq() == prodSeq) {
					result.add(dto);
					break;
				}
			}
		}
		allPromo = result;
		System.out.println(allPromo.size());
	}

	public List<String> suggestProdForPromoSearch(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public void clearPromoSearch() throws IOException {
		allPromo = promoService.getActive();
		for (PromotionDto dto: allPromo) {
			dto.setProductList(promoService.getProductList(dto.getSeq()));
		}
	}

	public void clearProdSearch() throws IOException {
		products = productService.getAll();
	}

	public boolean isAddFaqMode() {
		return addFaqMode;
	}

	public void setAddFaqMode(boolean addFaqMode) {
		this.addFaqMode = addFaqMode;
	}

	public FaqDto getNewFaq() {
		return newFaq;
	}

	public void setNewFaq(FaqDto newFaq) {
		this.newFaq = newFaq;
	}

	private boolean addFaqMode = false;
	private FaqDto newFaq = new FaqDto();
	private boolean viewFaqMode = false;

	public void startAddFaq(ProductDto prod) {
		newFaq.setProduct(prod);
		addFaqMode = true;
	}

	public void cancelAddFaq() {
		newFaq = new FaqDto();
		addFaqMode = false;
	}

	@Inject FaqService faqService;
	public void addFaq() throws IOException {
		if (newFaq.getQuestion() == null || newFaq.getQuestion().isEmpty() ||
				newFaq.getAnswer() == null || newFaq.getAnswer().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Question and answer are required", null);
			return;
		}

		faqService.addFaq(newFaq);
		faq.add(newFaq);

		ProductDto temp = newFaq.getProduct();

		addFaqMode = false;
		newFaq = new FaqDto();
		newFaq.setProduct(temp);

		sessionManager.addGlobalMessageInfo("New FAQ added", null);
	}

	public boolean isViewFaqMode() {
		return viewFaqMode;
	}

	public void setViewFaqMode(boolean viewFaqMode) {
		this.viewFaqMode = viewFaqMode;
	}

	public void startAddNewFaq() {
		addFaqMode = true;
	}

	public void cancelViewFaq() {
		viewFaqMode = false;
		addFaqMode = false;
		newFaq = new FaqDto();
	}

	public List<FaqDto> getFaq() {
		return faq;
	}

	public void setFaq(List<FaqDto> faq) {
		this.faq = faq;
	}

	private List<FaqDto> faq = new ArrayList<FaqDto>();

	public void startViewFaq(ProductDto prod) throws IOException {
		System.out.println("ProductController.startViewFaq()");
		newFaq = new FaqDto();
		newFaq.setProduct(prod);
		System.out.println(prod.getSeq());
		viewFaqMode = true;
		faq = faqService.getByProduct(prod.getSeq());
		faqProd = prod;
	}

	private List<CategoryDto> catList = new ArrayList<CategoryDto>();
	private String newCategory;

	@Inject CategoryService catService;

	public List<String> suggestCategory(String partial) throws IOException {
		System.out.println("ProductController.suggestCategory()");
		List<CategoryDto> temp = catService.searchCategory(partial);
		List<String> result = new ArrayList<String>();
		for (CategoryDto dto: temp) {
			result.add(dto.getCategory());
		}
		return result;
	}

	public void categorySelect() {
		catList.add(new CategoryDto(newCategory));
		newCategory = "";
	}

	public void deleteSelectedCat() {
		System.out.println("ProductController.deleteSelectedCat()");
		for (int i = getCatList().size() - 1; i >=0; i--) {
			System.out.println(catList.get(i).getCategory() + " " + catList.get(i).isSelected());
			if (getCatList().get(i).isSelected()) {
				System.out.println(catList.get(i).getCategory());
				getCatList().remove(i);
			}
		}
	}

	public List<CategoryDto> getCatList() {
		return catList;
	}

	public void setCatList(List<CategoryDto> catList) {
		this.catList = catList;
	}

	public String getNewCategory() {
		return newCategory;
	}

	public void setNewCategory(String newCategory) {
		this.newCategory = newCategory;
	}

	private ProductDto faqProd;

	public void handleFileUpload(FileUploadEvent event) {
		try {
	    	UploadedFile file = event.getFile();
	        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date()) + sessionManager.getLoginUser().getId() + event.getFile().getFileName();
	        File f = new File(path + name);
	        InputStream is = event.getFile().getInputstream();
	        OutputStream out = new FileOutputStream(f);
	        byte buf[] = new byte[1024];
	        int len;
	        while ((len = is.read(buf)) > 0)
	            out.write(buf, 0, len);
	        is.close();
	        out.close();

	        String errorMsg = readEmpListFromExcel(path + name);
	        if (errorMsg.isEmpty())
	        	sessionManager.addGlobalMessageInfo("Records added successfully", null);
	        else
	        	sessionManager.addGlobalMessageInfo(errorMsg, null);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	private String readEmpListFromExcel(String fullName) throws IOException {
		String msg = "";
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fullName));

		HSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 0; r < rows - 1; r++) {
			HSSFRow row = sheet.getRow(r + 1);
			if (row == null) {
				continue;
			}

			try {
				String q = row.getCell(0).getStringCellValue();
				String a = row.getCell(1).getStringCellValue();
				FaqDto faqDto = new FaqDto(faqService.getSeq(), faqProd, q, a);
				faqService.addFaq(faqDto);
				faq.add(faqDto);
			} catch (Exception e) {
				msg += e.getMessage();
			}
		}
		return msg;
	}

	public ProductDto getFaqProd() {
		return faqProd;
	}

	public void setFaqProd(ProductDto faqProd) {
		this.faqProd = faqProd;
	}
}
