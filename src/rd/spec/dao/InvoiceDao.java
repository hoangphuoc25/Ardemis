package rd.spec.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;

public interface InvoiceDao {
	public List<InvoiceDto> getAll(Transaction transaction) throws IOException;

	public InvoiceDto getById(Transaction transaction, int seq) throws IOException;
	public void deleteById(Transaction transaction, int seq) throws IOException;
	public List<ProductDto> getProductByInvoiceId (Transaction transaction, int seq) throws IOException;
	public void addInvoice(Transaction transaction, InvoiceDto invoice) throws IOException;
	public List<InvoiceDto> getByCustomer(Transaction transaction, int seq) throws IOException;
	public void updateInvoice(Transaction transaction, InvoiceDto invoice) throws IOException;
	public List<InvoiceDto> findInvoicesByProduct(Transaction transaction, int seq) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<InvoiceDto> searchInvoiceBeforeDate(Transaction transaction, Date date) throws IOException;
	public List<InvoiceDto> searchInvoiceAfterDate(Transaction transaction, Date date) throws IOException;
	public List<InvoiceDto> searchInvoiceBeforeAfter(Transaction transaction, Date after, Date before) throws IOException;
	public List<CompanyDto> findCompanyByProduct(Transaction transaction, int seq) throws IOException;
	public List<InvoiceDto> getBySalesperson(Transaction transaction, String userId) throws IOException;
	public List<InvoiceDto> getExpiringPurchase(Transaction transaction) throws IOException;
	public List<InvoiceDto> searchInvoiceByCustomerName(Transaction transaction, String name) throws IOException;
	public List<InvoiceDto> searchInvoiceByCompanyName(Transaction transaction, String name) throws IOException;
}
