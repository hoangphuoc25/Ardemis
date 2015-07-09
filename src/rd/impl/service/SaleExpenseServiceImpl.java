package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.SaleExpenseDto;
import rd.spec.dao.SaleExpenseDao;
import rd.spec.dao.Transaction;
import rd.spec.service.SaleExpenseService;

public class SaleExpenseServiceImpl implements SaleExpenseService, Serializable {

	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private SaleExpenseDao saleExpenseDao;

	@Inject
	public SaleExpenseServiceImpl(Transaction transaction,
			SaleExpenseDao saleExpenseDao) {
		this.transaction = transaction;
		this.saleExpenseDao = saleExpenseDao;
	}

	public void addSaleExpense(SaleExpenseDto se) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			saleExpenseDao.addSaleExpense(transaction, se);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public SaleExpenseDto getSaleExpenseById(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			SaleExpenseDto result = saleExpenseDao.getSaleExpenseById(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateSaleExpense(SaleExpenseDto se) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			saleExpenseDao.updateSaleExpense(transaction, se);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteSaleExpense(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			saleExpenseDao.deleteSaleExpense(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public int getSeq() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			int seq = saleExpenseDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<SaleExpenseDto> getBySalepersonId(String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<SaleExpenseDto> all = saleExpenseDao.getBySalepersonId(transaction, id);
			transaction.commit();
			return all;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
