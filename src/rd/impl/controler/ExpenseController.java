package rd.impl.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ProductDto;
import rd.dto.RoleDto;
import rd.dto.SaleExpenseDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.SaleExpenseService;

@Named
@ConversationScoped
public class ExpenseController implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject Conversation conversation;
	@Inject SessionManager sessionManager;
	@Inject SaleExpenseService expService;

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

	public SaleExpenseDto getNewExp() {
		return newExp;
	}

	public void setNewExp(SaleExpenseDto newExp) {
		this.newExp = newExp;
	}

	private SaleExpenseDto newExp = new SaleExpenseDto();
	private List<SaleExpenseDto> expenses;
	private List<SaleExpenseDto> myExp;

	public void addNewExp() throws IOException {
		newExp.setSeq(expService.getSeq());
		newExp.setSalesperson(sessionManager.getLoginUser());
		expService.addSaleExpense(newExp);
		myExp.add(newExp);
		sessionManager.addGlobalMessageInfo("Info added successfully", null);
		newExp = new SaleExpenseDto();
	}

	public List<SaleExpenseDto> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<SaleExpenseDto> expenses) {
		this.expenses = expenses;
	}

	public void validateReceiptDate(FacesContext context, UIComponent component, Object value) throws IOException {
		Date d = (Date) value;
		if (d.getTime() > (new Date()).getTime())
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date.", null));
    }

	public void validateAmount(FacesContext context, UIComponent component, Object value) throws IOException {
		double d = (Double) value;
		if (d < 0)
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid amount.", null));
	}

	public List<SaleExpenseDto> getMyExp() throws IOException {
		if (myExp == null || myExp.size() == 0) {
			myExp = expService.getBySalepersonId(sessionManager.getLoginUser().getId());
		}
		return myExp;
	}

	public void setMyExp(List<SaleExpenseDto> myExp) {
		this.myExp = myExp;
	}


	public void handleFileUpload(FileUploadEvent event) {
		reload();
		try {
	    	UploadedFile file = event.getFile();
	        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date()) + sessionManager.getLoginUser().getId() +
	        		event.getFile().getFileName();
	        logger.error(path);
	        File f = new File(path + name);
	        logger.error(path + name);
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
	        	sessionManager.addGlobalMessageInfo("Recorded added successfully", null);
	        else
	        	sessionManager.addGlobalMessageInfo(errorMsg, null);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		conversationEnd();
    }

    public String readEmpListFromExcel(String fullFileName) throws FileNotFoundException, IOException {
    	String errorMsg = "";

		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fullFileName));

		HSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 0; r < rows - 1; r++) {
			HSSFRow row = sheet.getRow(r + 1);
			if (row == null) {
				continue;
			}

			Date date = row.getCell(0).getDateCellValue();
			String purpose = row.getCell(1).getStringCellValue();
			String receiptNo = "";
			if (row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				receiptNo = String.valueOf(row.getCell(2).getNumericCellValue());
				System.out.println("numeric type");
			} else if (row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				receiptNo = row.getCell(2).getStringCellValue();
				System.out.println("string type");
			}

			double amount = 0;

			if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				amount = row.getCell(3).getNumericCellValue();
			} else if (row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				amount = Double.parseDouble(row.getCell(3).getStringCellValue());
			}

			System.out.println(date);
			System.out.println(purpose);
			System.out.println(receiptNo);
			System.out.println(amount);

			if (amount < 0) {
				errorMsg += "Invalid amount at row " + (r + 1) + ". Data at row " + (r+1) + " is not inserted.\n";
				continue;
			}
			if (date.getTime() > (new Date()).getTime()) {
				errorMsg += "Invalid receipt date at row " + (r + 1) + ". Data at row " + (r+1) + " is not inserted.\n";
				continue;
			}
			SaleExpenseDto expense = new SaleExpenseDto(expService.getSeq(), sessionManager.getLoginUser(), date, purpose, receiptNo, amount);
			expService.addSaleExpense(expense);
			myExp.add(expense);
		}
    	return errorMsg;
    }

    public void remove(SaleExpenseDto expense) throws IOException {
    	reload();
    	for (int i = myExp.size() - 1; i >= 0; i--) {
    		if (myExp.get(i).getSeq() == expense.getSeq()) {
    			expService.deleteSaleExpense(myExp.get(i).getSeq());
    			myExp.remove(i);
    			break;
    		}
    	}
    }

    public void onRowEdit(RowEditEvent event) throws IOException {
    	SaleExpenseDto obj = (SaleExpenseDto) event.getObject();
    	if (obj.getAmount() <= 0) {
    		sessionManager.addGlobalMessageFatal("Invalid amount.", null);
    		restoreObj(event);
    		return;
    	}

    	if (obj.getPurpose().isEmpty()) {
    		sessionManager.addGlobalMessageFatal("Purpose is required.", null);
    		restoreObj(event);
    		return;
    	}

    	if (obj.getReceiptNo().isEmpty()) {
    		sessionManager.addGlobalMessageFatal("Invalid No.", null);
    		restoreObj(event);
    		return;
    	}

    	if (obj.getReceiptDate().getTime() > (new Date()).getTime()) {
    		sessionManager.addGlobalMessageFatal("Invalid Date", null);
    		restoreObj(event);
    		return;
    	}


    	for (int i = myExp.size() - 1; i >= 0; i--) {
    		if (myExp.get(i).getSeq() == obj.getSeq()) {
    			expService.updateSaleExpense(obj);
    			myExp.set(i, obj);
    			System.out.println("removed");
    			sessionManager.addGlobalMessageInfo("Info updated successully", null);
    			break;
    		}
    	}
    }

    public void onRowEditCancel(RowEditEvent event) {

    }

    public void restoreObj(RowEditEvent event) throws IOException {
    	SaleExpenseDto obj = (SaleExpenseDto) event.getObject();
    	for (int i = myExp.size() - 1; i >= 0; i--) {
    		if (myExp.get(i).getSeq() == obj.getSeq()) {
    			myExp.set(i, expService.getSaleExpenseById(obj.getSeq()));
    			System.out.println("restored");
    			break;
    		}
    	}
    }

    @Inject SessionController sessionController;
	public double deduceCost(SaleExpenseDto se) {
		if (sessionController.getCurrency() == 0)
			return se.getAmount();
		return se.getAmount()*sessionController.getRates().get(sessionController.getCurrency());
	}
}
