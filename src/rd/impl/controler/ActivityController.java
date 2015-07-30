package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.ActivityDto;
import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.dto.SaleTargetDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ActivityService;
import rd.spec.service.CompanyService;
import rd.spec.service.InvoiceService;
import rd.spec.service.ProductService;
import rd.spec.service.SaleTargetService;

@Named
@ConversationScoped
public class ActivityController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject CompanyService compService;
	@Inject ActivityService actService;
	@Inject SessionManager sessionManager;
	@Inject InvoiceService invoiceService;
	@Inject ProductService prodService;
	@Inject SaleTargetService stService;

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

	public ActivityDto getNewAct() {
		if (newAct == null) {
			newAct = new ActivityDto();
		}
		return newAct;
	}

	public void setNewAct(ActivityDto newAct) {
		this.newAct = newAct;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public List<String> getStatusOptions() {
		if (statusOptions == null) {
			statusOptions = new ArrayList<String>();
			statusOptions.add("Contacted");
			statusOptions.add("Negotiating");
			statusOptions.add("Completed");
			statusOptions.add("Halted");
		}
		return statusOptions;
	}

	public void setStatusOptions(List<String> statusOptions) {
		this.statusOptions = statusOptions;
	}

	public List<ActivityDto> getAllAct() throws IOException {
		if (allAct == null) {
			allAct = actService.getByUser(sessionManager.getLoginUser().getId());
		}
		return allAct;
	}

	public void setAllAct(List<ActivityDto> allAct) {
		this.allAct = allAct;
	}

	private String compName;
	private ActivityDto newAct;
	private List<String> statusOptions;
	private List<ActivityDto> allAct;
	private List<String> statusOptionsEditBox;

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp =  compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public void addActivity() throws IOException {
		if (compName == null || compName.isEmpty()) {
			sessionManager.addGlobalMessageFatal("Please enter company name", null);
			return;
		}
		if (newAct.getStartDate().getTime() > (new Date()).getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid date", null);
			return;
		}

		int seq = actService.getSeq();
		newAct.setSeq(seq);
		newAct.setSalesperson(sessionManager.getLoginUser());
		CompanyDto comp = compService.getById(Integer.parseInt(compName.split("[()]")[1]));
		newAct.setCustomer(comp);
		actService.addActivity(newAct);
		allAct.add(newAct);
		newAct = new ActivityDto();
		sessionManager.addGlobalMessageFatal("New sale activity added", null);
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public ActivityDto getSelectedAct() {
		if (selectedAct == null) {
			selectedAct = new ActivityDto();
		}
		return selectedAct;
	}

	public void setSelectedAct(ActivityDto selectedAct) {
		this.selectedAct = selectedAct;
	}

	private boolean editMode = false;
	private ActivityDto selectedAct;

	public void startEdit(ActivityDto act) {
		editMode = true;
		selectedAct = act;
	}

	public void editAct() throws IOException {
		actService.updateActivity(selectedAct);
		for (int i = 0; i < allAct.size(); i++) {
			if (allAct.get(i).getSeq() == selectedAct.getSeq()) {
				allAct.set(i, selectedAct);
				break;
			}
		}
		editMode = false;
	}

	public boolean isAddInvoiceMode() {
		return addInvoiceMode;
	}

	public void setAddInvoiceMode(boolean addInvoiceMode) {
		this.addInvoiceMode = addInvoiceMode;
	}

	private boolean addInvoiceMode = false;

	public void startAddNewPurchase(ActivityDto act) {
		System.out.println("ActivityController.startAddNewPurchase()");
		addInvoiceMode = true;
		selectedAct = act;
	}

	private InvoiceDto newInvoice;
	private List<ProductDto> selectedProducts;
	private String search;

	public void addNewInvoice() throws IOException {
		if (getSelectedProducts() == null || getSelectedProducts().size() == 0) {
			sessionManager.addGlobalMessageFatal("Product list empty", null);
			return;
		}
		if (newInvoice.getPurchaseDate() == null) {
			sessionManager.addGlobalMessageFatal("Invalid date", null);
			return;
		}
		getNewInvoice().setSeq(invoiceService.getSeq());

		getNewInvoice().setCustomer(selectedAct.getCustomer());
		double amount = 0;
		for (ProductDto dto: getSelectedProducts()) {
			amount += dto.getPrice();
		}
		getNewInvoice().setProducts(getSelectedProducts());
		getNewInvoice().setAmount(amount);
		invoiceService.addInvoice(getNewInvoice());

		getStd().setCurrent(getStd().getCurrent() + (int) Math.ceil(amount));
		stService.updateSaleTarget(std);

		sessionManager.addGlobalMessageInfo("New purchase record added", null);
		setSearch("");
		setSelectedProducts(new ArrayList<ProductDto>());
		newInvoice = new InvoiceDto();

		selectedAct.setStatus("Completed");
		editAct();
		addInvoiceMode = false;
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

	public List<String> suggest(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public List<ProductDto> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<ProductDto> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public void select() throws IOException {
		if (getSelectedProducts() == null) {
			setSelectedProducts(new ArrayList<ProductDto>());
		}
		int seq = Integer.parseInt(getSearch().split("[()]")[1]);
		ProductDto prod = prodService.getProductById(seq);
		for (int i = 0; i < selectedProducts.size(); i++) {
			if (selectedProducts.get(i).getSeq() == seq)
				return;
		}
		selectedProducts.add(prod);
	}

	public void cancelEditAct() {
		editMode = false;
		selectedAct = new ActivityDto();
	}

	public void cancelAddInvoice() {
		addInvoiceMode = false;
		newInvoice = new InvoiceDto();
	}

	public void deleteSelected() {
		for (Iterator<ProductDto> iterator = getSelectedProducts().iterator(); iterator.hasNext();) {
		    ProductDto t = iterator.next();
		    if (t.isSelected()) {
		        iterator.remove();
		    }
		}
		System.out.println("ActivityController.deleteSelected()");
	}

	public SaleTargetDto getStd() throws IOException {
		if (std == null) {
			std = stService.getSaleTarget(sessionManager.getLoginUser().getId());
		}
		return std;
	}

	public void setStd(SaleTargetDto std) {
		this.std = std;
	}

	public List<String> getStatusOptionsEditBox() {
		if (statusOptionsEditBox == null) {
			statusOptionsEditBox = new ArrayList<String>();
			statusOptionsEditBox.add("Contacted");
			statusOptionsEditBox.add("Negotiating");
			statusOptionsEditBox.add("Halted");
		}
		return statusOptionsEditBox;
	}

	public void setStatusOptionsEditBox(List<String> statusOptionsEditBox) {
		this.statusOptionsEditBox = statusOptionsEditBox;
	}

	private SaleTargetDto std;
}

