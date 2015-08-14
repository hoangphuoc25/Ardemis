package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.ActivityDto;
import rd.dto.ProductDto;
import rd.spec.dao.ActivityDao;
import rd.spec.dao.Transaction;
import rd.spec.service.ActivityService;

public class ActivityServiceImpl implements ActivityService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;

	private ActivityDao activityDao;

	@Inject
	public ActivityServiceImpl(Transaction transaction, ActivityDao actDao) {
		this.transaction = transaction;
		this.activityDao = actDao;
	}

	public void addActivity(ActivityDto act) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			activityDao.addActivity(transaction, act);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public ActivityDto getById(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			ActivityDto result = activityDao.getById(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<ActivityDto> getByUser(String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<ActivityDto> result = activityDao.getByUser(transaction, username);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateActivity(ActivityDto act) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			activityDao.updateActivity(transaction, act);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteActivity(ActivityDto act) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			activityDao.deleteActivity(transaction, act);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public int getSeq() throws IOException {
		try {
			transaction.begin();
			int seq = activityDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ActivityDto> findByStatus(String status, String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ActivityDto> result = activityDao.findByStatus(transaction, status, username);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ActivityDto> getActiveDeal() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ActivityDto> result = activityDao.getActiveDeal(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ProductDto> getProductByDeal(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> result = activityDao.getProductByDeal(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ActivityDto> searchByCustomerName(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ActivityDto> result = activityDao.searchByCustomerName(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
