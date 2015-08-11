package rd.spec.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.CompanyDto;
import rd.dto.InvoiceDto;
import rd.dto.ProductDto;

public interface InvoiceService {
	public List<InvoiceDto> getAll() throws IOException;
	public InvoiceDto getById(int seq) throws IOException;
	public void deleteById(int seq) throws IOException;
	public List<ProductDto> getProductByInvoiceId(int seq) throws IOException;
	public void addInvoice(InvoiceDto invoice) throws IOException;
	public List<InvoiceDto> getByCustomer(int seq) throws IOException;
	public void updateInvoice(InvoiceDto invoice) throws IOException;
	public List<InvoiceDto> findInvoicesByProduct(int seq) throws IOException;
	public int getSeq() throws IOException;
	public List<InvoiceDto> searchInvoiceBeforeDate(Date date) throws IOException;
	public List<InvoiceDto> searchInvoiceAfterDate(Date date) throws IOException;
	public List<InvoiceDto> searchInvoiceBeforeAfter(Date after, Date before) throws IOException;
	public List<CompanyDto> findCompanyByProduct(int seq) throws IOException;
	public List<InvoiceDto> getBySalesperson(String userId) throws IOException;
	public List<InvoiceDto> getExpiringPurchase() throws IOException;
}
