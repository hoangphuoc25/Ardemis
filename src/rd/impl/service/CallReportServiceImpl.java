package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;

import rd.dto.CallReportDto;
import rd.spec.dao.CallReportDao;
import rd.spec.dao.Transaction;
import rd.spec.service.CallReportService;

public class CallReportServiceImpl implements CallReportService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private CallReportDao callReportDao;

	@Inject
	public CallReportServiceImpl(Transaction transaction,
			CallReportDao callReportDao) {
		this.callReportDao = callReportDao;
		this.transaction = transaction;
	}

	public void addReport(CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			callReportDao.addReport(transaction, report);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public CallReportDto getById(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			CallReportDto cr = callReportDao.getById(transaction, seq);
			transaction.commit();
			return cr;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateCallReport(CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			callReportDao.updateCallReport(transaction, report);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteCallReport(CallReportDto cr) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			callReportDao.deleteCallReport(transaction, cr);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
