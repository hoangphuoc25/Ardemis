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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CallReportDto;
import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.dto.TeamDto;
import rd.dto.UserDto;
import rd.dto.WrUserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CallReportService;
import rd.spec.service.CompanyService;
import rd.spec.service.ContactService;
import rd.spec.service.InvoiceService;
import rd.spec.service.ProductService;
import rd.spec.service.TeamService;
import rd.spec.service.UserService;
import rd.utils.AppUtil;
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
	@Inject UserService userService;

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
		if (getStatus() == null || getStatus().isEmpty()) {
			showingMode = "all";
		} else {
			showingMode = getStatus();
		}
		return showingMode;
	}

	public void setShowingMode(String showingMode) {
		this.showingMode = showingMode;
	}

	private String showingMode;

	public void updateCompanyList() throws IOException {
		if (getShowingMode().equals("all"))
			customerList = comService.getAll();
		else if (getShowingMode().equalsIgnoreCase("new"))
			customerList = comService.getCompanyByContactStatusAndUser(showingMode, sessionManager.getLoginUser().getId());
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

	public boolean isAddContactMode() {
		return addContactMode;
	}

	public void setAddContactMode(boolean addContactMode) {
		this.addContactMode = addContactMode;
	}

	private boolean addContactMode = false;
	private ContactDto newContact = new ContactDto();
	private List<ContactDto> contactList;

	@Inject ContactService contactService;

	public ContactDto getNewContact() {
		return newContact;
	}
	public void setNewContact(ContactDto newContact) {
		this.newContact = newContact;
	}

	public void startAddContact(CompanyDto comp) throws IOException {
		newContact.setCompany(comp.getName());
		addContactMode = true;
		assigneeName = sessionManager.getLoginUser().getName() + "(" + sessionManager.getLoginUser().getId() + ")";
	}

	public void startAddContact() throws IOException {
		addContactMode = true;
		newContact.setAssignee(sessionManager.getLoginUser());
	}
	public void addNewContact() throws IOException {
		if (newContact.getName() == null || newContact.getName().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Customer name required", null);
			return;
		}
		if (newContact.getPhone() == null || newContact.getPhone().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Customer phone number required.", null);
			return;
		}
		if (!validatePhone(newContact.getPhone())) {
			sessionManager.addGlobalMessageFatal("Invalid phone number.", null);
			return;
		}
		if (assigneeName == null || assigneeName.isEmpty()) {
			newContact.setAssignee(sessionManager.getLoginUser());
			contactList.add(0, newContact);
		} else {
			UserDto assignee = userService.findUserById(assigneeName.split("[()]")[1]);
			newContact.setAssignee(assignee);
		}
		newContact.setSeq(contactService.getSeq());
		newContact.setContactStatus(AppUtil.getStatus(newContact.getSource()));
		contactService.addContact(newContact);
		addContactMode = false;
		newContact = new ContactDto();
		sessionManager.addGlobalMessageInfo("New customer added", null);
	}
	public void cancelAddNewContact() {
		addContactMode = false;
		setNewContact(new ContactDto());
	}

	public void searchContactByName(String name) throws IOException {
		contactList = contactService.searchContactByName(name);
	}

	public List<ContactDto> getContactList() throws IOException {
		if (contactList == null && (status == null || status.isEmpty())) {
			contactList = contactService.getAll();
		} else if (contactList == null && !status.isEmpty() && status.equalsIgnoreCase("new")) {
			contactList = contactService.getByStatusAndUser("new", sessionManager.getLoginUser().getId());
			contactList.addAll(contactService.getByStatusAndUser("pre-qualified", sessionManager.getLoginUser().getId()));
			showingMode = status;
		} else if (contactList == null && !status.isEmpty()) {
			contactList = contactService.getByStatusAndUser(status, sessionManager.getLoginUser().getId());
			showingMode = status;
		}
		return contactList;
	}
	public void setContactList(List<ContactDto> contactList) {
		this.contactList = contactList;
	}

	public boolean isEditContactMode() {
		return editContactMode;
	}
	public void setEditContactMode(boolean editContactMode) {
		this.editContactMode = editContactMode;
	}

	private boolean editContactMode;

	public void startEditContact(ContactDto contact) {
		newContact = contact;
		editContactMode = true;
	}
	public void editContact() throws IOException {
		if (newContact.getName() == null || newContact.getName().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Customer name required", null);
			return;
		}
		if (newContact.getPhone() == null || newContact.getPhone().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Customer phone number required.", null);
			return;
		}
		if (!validatePhone(newContact.getPhone())) {
			sessionManager.addGlobalMessageFatal("Invalid phone number.", null);
			return;
		}
		if (assigneeName != null && !assigneeName.isEmpty()) {
			UserDto newAss = userService.findUserById(assigneeName.split("[()]")[1]);
			newContact.setAssignee(newAss);
			for (int i = contactList.size() - 1; i >= 0; i--) {
				if (contactList.get(i).getSeq() == newContact.getSeq()) {
					contactList.remove(i);
					break;
				}
			}
		}
		contactService.updateContact(newContact);
		editContactMode = false;
		sessionManager.addGlobalMessageInfo("Customer info updated.", null);
		assigneeName = "";
	}
	public void cancelEditContact() {
		newContact = new ContactDto();
		editContactMode = false;
	}

	public void updateContactList() throws IOException {
		if (showingMode.equals("all"))
			contactList = contactService.getAll();
		else if (showingMode.equalsIgnoreCase("new")) {
			contactList = contactService.getByStatusAndUser(showingMode, sessionManager.getLoginUser().getId());
			contactList.addAll(contactService.getByStatusAndUser("pre-qualified", sessionManager.getLoginUser().getId()));
		}
		else
			contactList = contactService.getByStatus(showingMode);
		purchasedProduct = "";
	}

	public void deleteContact(ContactDto contact) throws IOException {
		for (int i = contactList.size() - 1; i >= 0; i--) {
			if (contactList.get(i).getSeq() == contact.getSeq()) {
				contactList.remove(i);
				break;
			}
		}
		contactService.deleteContact(contact.getSeq());
	}

	public String callReportLink(ContactDto cont) {
		return "callReport.jsf?contactSeq=" + cont.getSeq() + "&back=true";
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
	    	UploadedFile file = event.getFile();
	        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date()) + sessionManager.getLoginUser().getId() +
	        		event.getFile().getFileName();
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

		List<ContactDto> contacts = new ArrayList<ContactDto>();
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		for (int r = 0; r < rows - 1; r++) {
			HSSFRow row = sheet.getRow(r + 1);
			evaluator.evaluateInCell(row.getCell(1));
			evaluator.evaluateInCell(row.getCell(2));
			evaluator.evaluateInCell(row.getCell(3));
			evaluator.evaluateInCell(row.getCell(4));
			evaluator.evaluateInCell(row.getCell(5));
		}

		for (int r = 0; r < rows - 1; r++) {
			HSSFRow row = sheet.getRow(r + 1);
			if (row == null) {
				continue;
			}

			String name = "";
			if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				name = String.valueOf(row.getCell(0).getNumericCellValue());
			} else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				name = row.getCell(0).getStringCellValue();
			} else if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				CellValue cellValue = evaluator.evaluate(row.getCell(0));
				name = cellValue.getStringValue();
			}
			System.out.println(row.getCell(0).getCellType());
			String company = row.getCell(1).getStringCellValue();
			String phone = "";
			if (row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				phone = String.valueOf(row.getCell(2).getNumericCellValue());
			} else if (row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				phone = row.getCell(2).getStringCellValue();
			}

			String email = row.getCell(3).getStringCellValue();
			String gender = row.getCell(4).getStringCellValue();
			String address = row.getCell(5).getStringCellValue();
			String source = row.getCell(6).getStringCellValue();
			String status = "";
			List<String> newQualifier = new ArrayList<String>(Arrays.asList("name card", "advertisement"));
			List<String> preQualifiedQualifier = new ArrayList<String>(Arrays.asList("conference", "trade show", "web", "personal contact"));
			if (newQualifier.contains(source.toLowerCase())) {
				status = "New";
			} else if (preQualifiedQualifier.contains(source.toLowerCase())) {
				status = "Pre-qualified";
			}

			if (name.isEmpty()) {
				msg += "Row " + (r+2) + ": Name is required";
				System.out.println("name invalid at row" + (r+2));
				continue;
			}
			if (!validatePhone(phone)) {
				msg += "Row " + (r+2) + ": Phone number is invalid.";
				System.out.println("phone invalid at row" + (r+2));
				continue;
			}

			ContactDto contact = new ContactDto(contactService.getSeq(), name, gender, phone, email, company, "English", address, sessionManager.getLoginUser(), status, source);
			contacts.add(contact);
			//contactService.addContact(contact);
		}

		List<UserDto> sales = userService.getUserByRole("sale");
		List<WrUserDto> wrSales = new ArrayList<WrUserDto>();
		for (UserDto dto: sales) {
			wrSales.add(new WrUserDto(dto, contactService.getNumberOfContactPerSale(dto.getId())));
		}
		Collections.sort(wrSales, new Comparator<WrUserDto>() {
			public int compare(WrUserDto f1, WrUserDto f2) {
				if (f1.getAssignedContacts() == f2.getAssignedContacts()) {
					return 0;
				} else if (f1.getAssignedContacts() > f2.getAssignedContacts()) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		int currentNoAssigned = 0;
		for (WrUserDto dto: wrSales) {
			System.out.println(dto.getAssignedContacts());
			currentNoAssigned += dto.getAssignedContacts();
		}

		int newAvg = (currentNoAssigned + contacts.size()) / sales.size() + 1;
		System.out.println("SalespersonController.readEmpListFromExcel()");
		System.out.println(currentNoAssigned + " " + contacts.size());
		System.out.println(newAvg);
		int i = 0;
		for (ContactDto contact: contacts) {
			if (wrSales.get(i).current > newAvg) {
				i = (i + 1) % wrSales.size();
			}
			contact.setAssignee(wrSales.get(i).getSale());
			contactService.addContact(contact);
			wrSales.get(i).current++;
			i = (i + 1) % wrSales.size();
		}

//		for (WrUserDto wrud: wrSales) {
//			for (int j = 0; j < newAvg - wrud.getAssignedContacts(); j++) {
//				contacts.get(j+i).setAssignee(wrud.getSale());
//				contactService.addContact(contacts.get(j+i));
//			}
//			i += newAvg - wrud.getAssignedContacts();
//			System.out.println("i: " + i);
//		}
		for (WrUserDto wrud: wrSales) {
			System.out.println(wrud.current);
		}

		return msg;
	}

	private boolean validatePhone(String p) {
		String phone = p.replace(" ", "");
		String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		if (!matcher.matches()) {
			return false;
		} else {
			return true;
		}
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	private String assigneeName;

	public boolean showCallLink(ContactDto contact) throws IOException {
		if (contact.getAssignee().getId().equalsIgnoreCase(sessionManager.getLoginUser().getId())) {
			return true;
		} else
			return false;
	}

	public boolean isViewContactDetailMode() {
		return viewContactDetailMode;
	}

	public void setViewContactDetailMode(boolean viewContactDetailMode) {
		this.viewContactDetailMode = viewContactDetailMode;
	}

	public List<CallReportDto> getPastCalls() {
		return pastCalls;
	}

	public void setPastCalls(List<CallReportDto> pastCalls) {
		this.pastCalls = pastCalls;
	}

	public List<InvoiceDto> getPastPurchase() {
		return pastPurchase;
	}

	public void setPastPurchase(List<InvoiceDto> pastPurchase) {
		this.pastPurchase = pastPurchase;
	}

	private boolean viewContactDetailMode;
	private List<InvoiceDto> pastPurchase;
	private List<CallReportDto> pastCalls;

	public void startViewContactDetail(ContactDto contact) throws IOException {
		pastPurchase = invoiceService.getByCustomer(contact.getSeq());
		pastCalls = crService.getByContact(contact.getSeq());
		viewContactDetailMode = true;
	}

	public void closeViewContactDetail() {
		viewContactDetailMode = false;
	}
}

