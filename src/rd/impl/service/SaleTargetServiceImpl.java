package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;

import rd.dto.SaleTargetDto;
import rd.spec.dao.SaleTargetDao;
import rd.spec.dao.Transaction;
import rd.spec.service.SaleTargetService;

public class SaleTargetServiceImpl implements SaleTargetService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;

	private Transaction transaction;
	private SaleTargetDao saleTargetDao;

	@Inject
	public SaleTargetServiceImpl(Transaction transaction, SaleTargetDao saleTargetDao) {
		this.transaction = transaction;
		this.saleTargetDao = saleTargetDao;
	}

	public SaleTargetDto getSaleTarget(String saleId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			SaleTargetDto result = saleTargetDao.getSaleTarget(transaction, saleId);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addSaleTarget(SaleTargetDto std) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			saleTargetDao.addSaleTarget(transaction, std);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void updateSaleTarget(SaleTargetDto std) throws IOException {
		try {
			transaction.begin();
			saleTargetDao.updateSaleTarget(transaction, std);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}

	}
}
