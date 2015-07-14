package rd.impl.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;

import rd.dto.SaleExpenseDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.SaleExpenseService;

@Named
@ConversationScoped
public class EmployeeController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject SessionManager sessionManager;
	@Inject SaleExpenseService expService;

	private StreamedContent file;

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

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SaleExpenseDto> getExpList() throws IOException {
		if (expList == null || expList.size() == 0) {
			expList = expService.getBySalepersonId(id);
		}

		return expList;
	}

	public void setExpList(List<SaleExpenseDto> expList) {
		this.expList = expList;
	}

	public StreamedContent getFile() throws ParseException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
		Row rr = sheet.createRow(0);
		Cell cc = rr.createCell(0);
		cc.setCellValue("Name");
		cc = rr.createCell(1);
		cc.setCellValue("Receipt Date");
		cc = rr.createCell(2);
		cc.setCellValue("Purpose");
		cc = rr.createCell(3);
		cc.setCellValue("Receipt No");
		cc = rr.createCell(4);
		cc.setCellValue("Amount");

		for (int i = 0; i < expList.size(); i++) {
			Row row = sheet.createRow(i+1);
			Cell cell = row.createCell(0);
			cell.setCellValue(expList.get(i).getSalesperson().getName());
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			cell.setCellValue(sdf.format(expList.get(i).getReceiptDate()));
			cell = row.createCell(2);
			cell.setCellValue(expList.get(i).getPurpose());
			cell = row.createCell(3);
			cell.setCellValue(expList.get(i).getReceiptNo());
			cell = row.createCell(4);
			cell.setCellValue(expList.get(i).getAmount());
		}
		try {
	        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date()) + sessionManager.getLoginUser().getId() + "report.xls";
		    FileOutputStream out = new FileOutputStream(new File(path + name));
		    workbook.write(out);
		    out.close();
		    System.out.println("Excel written successfully..");
		    InputStream stream = new FileInputStream(path + name);
		    System.out.println(path + name);
			file = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "report.xls");
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		    e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ioexception");
		    e.printStackTrace();
		}
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	private List<SaleExpenseDto> expList;

	public String writeExcel() throws ParseException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
		for (int i = 0; i < expList.size(); i++) {
			Row row = sheet.createRow(i+1);
			Cell cell = row.createCell(0);
			cell.setCellValue(expList.get(i).getSalesperson().getName());
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(expList.get(i).getReceiptDate());
			cell = row.createCell(2);
			cell.setCellValue(expList.get(i).getPurpose());
			cell = row.createCell(3);
			cell.setCellValue(expList.get(i).getReceiptNo());
			cell = row.createCell(4);
			cell.setCellValue(deduceCost(expList.get(i)));
		}
		try {
	        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date()) + sessionManager.getLoginUser().getId() + "report.xls";
		    FileOutputStream out = new FileOutputStream(new File(path + name));
		    workbook.write(out);
		    out.close();
		    System.out.println("Excel written successfully..");
		    InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path + name);
			file = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "report.xls");
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		    e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ioexception");
		    e.printStackTrace();
		}
		return "employee.jsf?id="+id+"&faces-redirect=true";
	}

	@Inject SessionController sessionController;
	public double deduceCost(SaleExpenseDto se) {
		if (sessionController.getCurrency() == 0)
			return se.getAmount();
		return se.getAmount()*sessionController.getRates().get(sessionController.getCurrency());
	}

	public ScheduleModel getModel() {
		if (model == null) {
			model = new DefaultScheduleModel();
			model.addEvent(new DefaultScheduleEvent("title", new Date(), new Date()));
		}
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	private ScheduleModel model = new DefaultScheduleModel();

	private Date from;
	private Date to;
	private String title;

	public void addNewEvent() {
		reload();
		System.out.println("EmployeeController.addNewEvent()");
		model.addEvent(new DefaultScheduleEvent(title, from, to));
	}
}
