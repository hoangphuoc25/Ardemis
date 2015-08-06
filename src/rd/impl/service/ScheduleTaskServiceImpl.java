package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.ScheduleTaskDto;
import rd.spec.dao.ScheduleTaskDao;
import rd.spec.dao.Transaction;
import rd.spec.service.ScheduleTaskService;

public class ScheduleTaskServiceImpl implements ScheduleTaskService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private ScheduleTaskDao seDao;

	@Inject
	public ScheduleTaskServiceImpl(Transaction transaction, ScheduleTaskDao seDao) {
		this.transaction = transaction;
		this.seDao = seDao;
	}

	@Override
	public int getSeq() throws IOException {
		try {
			transaction.begin();
			int seq = seDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void addEvent(ScheduleTaskDto evt) throws IOException {
		try {
			transaction.begin();
			seDao.addEvent(transaction, evt);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void updateEvent(ScheduleTaskDto evt) throws IOException {
		try {
			transaction.begin();
			seDao.updateEvent(transaction, evt);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void deleteEvent(int seq) throws IOException {
		try {
			transaction.begin();
			seDao.deleteEvent(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public ScheduleTaskDto getById(int seq) throws IOException {
		try {
			transaction.begin();
			ScheduleTaskDto result = seDao.getById(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public List<ScheduleTaskDto> getByUser(String username) throws IOException {
		try {
			transaction.begin();
			List<ScheduleTaskDto> result = seDao.getByUser(transaction, username);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public List<ScheduleTaskDto> getByUserToday(String username)
			throws IOException {
		try {
			transaction.begin();
			List<ScheduleTaskDto> result = seDao.getByUserToday(transaction, username);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ScheduleTaskDto> getByCompany(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ScheduleTaskDto> result = seDao.getByCompany(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ScheduleTaskDto> getByContact(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ScheduleTaskDto> result = seDao.getByContact(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ScheduleTaskDto> getByDeal(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ScheduleTaskDto> result = seDao.getByDeal(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
