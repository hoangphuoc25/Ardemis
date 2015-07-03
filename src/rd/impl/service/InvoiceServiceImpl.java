package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.InvoiceDto;
import rd.dto.ProductDto;
import rd.spec.dao.InvoiceDao;
import rd.spec.dao.Transaction;
import rd.spec.service.InvoiceService;

public class InvoiceServiceImpl implements InvoiceService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private InvoiceDao invoiceDao;

	@Inject
	public InvoiceServiceImpl(Transaction transaction, InvoiceDao invoiceDao) {
		this.transaction = transaction;
		this.invoiceDao = invoiceDao;
	}

	public List<InvoiceDto> getAll() throws IOException {
		try {
			transaction.begin();
			List<InvoiceDto> result = invoiceDao.getAll(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public InvoiceDto getById(int seq) throws IOException {
		try {
			transaction.begin();
			InvoiceDto result = invoiceDao.getById(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteById(int seq) throws IOException {
		try {
			transaction.begin();
			invoiceDao.deleteById(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<ProductDto> getProductByInvoiceId(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<ProductDto> result = invoiceDao.getProductByInvoiceId(
					transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addInvoice(InvoiceDto invoice) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			invoiceDao.addInvoice(transaction, invoice);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<InvoiceDto> getByCustomer(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<InvoiceDto> result = invoiceDao
					.getByCustomer(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateInvoice(InvoiceDto invoice) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			invoiceDao.updateInvoice(transaction, invoice);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
