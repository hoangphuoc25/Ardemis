package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.InvoiceDto;
import rd.dto.SaleExpenseDto;
import rd.spec.service.InvoiceService;

@Named
@ConversationScoped
public class ReportController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
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
}
