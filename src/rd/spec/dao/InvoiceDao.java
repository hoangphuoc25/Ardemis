package rd.spec.dao;

import java.io.IOException;
import java.util.List;

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
}
