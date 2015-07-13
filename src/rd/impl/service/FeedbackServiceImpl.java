package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.FeedbackDto;
import rd.spec.dao.FeedbackDao;
import rd.spec.dao.Transaction;
import rd.spec.service.FeedbackService;

public class FeedbackServiceImpl implements FeedbackService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private FeedbackDao feedbackDao;

	@Inject
	public FeedbackServiceImpl(Transaction transaction, FeedbackDao feedbackDao) {
		this.transaction = transaction;
		this.feedbackDao = feedbackDao;
	}

	public List<FeedbackDto> getFeedbackByProduct(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<FeedbackDto> result = feedbackDao.getFeedbackByProduct(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public FeedbackDto getById(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			FeedbackDto result = feedbackDao.getById(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addFeedback(FeedbackDto fb) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			feedbackDao.addFeedback(transaction, fb);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public int getSeq() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			int seq = feedbackDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
