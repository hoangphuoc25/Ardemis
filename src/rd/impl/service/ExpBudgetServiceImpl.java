package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import rd.dto.ExpBudgetDto;
import rd.spec.dao.ExpBudgetDao;
import rd.spec.dao.Transaction;
import rd.spec.service.ExpBudgetService;

public class ExpBudgetServiceImpl implements ExpBudgetService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private ExpBudgetDao expBudgetDao;

	public ExpBudgetServiceImpl(Transaction transaction,
			ExpBudgetDao expBudgetDao) {
		this.transaction = transaction;
		this.expBudgetDao = expBudgetDao;
	}

	public void addBudget(ExpBudgetDto budget) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			expBudgetDao.addBudget(transaction, budget);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateBudget(ExpBudgetDto budget) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			expBudgetDao.updateBudget(transaction, budget);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteBudget(ExpBudgetDto budget) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			expBudgetDao.deleteBudget(transaction, budget);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<ExpBudgetDto> getBudgetByUser(String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<ExpBudgetDto> result = expBudgetDao.getBudgetByUser(
					transaction, userId);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public ExpBudgetDto getCurrentBudget(String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			ExpBudgetDto result = expBudgetDao.getCurrentBudget(transaction,
					userId);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
