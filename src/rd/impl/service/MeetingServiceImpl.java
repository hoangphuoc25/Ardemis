package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rd.dto.MeetingDto;
import rd.spec.dao.MeetingDao;
import rd.spec.dao.Transaction;
import rd.spec.service.MeetingService;

public class MeetingServiceImpl implements MeetingService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private MeetingDao meetingDao;

	@Inject
	public MeetingServiceImpl(Transaction transaction, MeetingDao meetingDao) {
		this.transaction = transaction;
		this.meetingDao = meetingDao;
	}

	public void addMeeting(MeetingDto meeting) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			meetingDao.addMeeting(transaction, meeting);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<MeetingDto> getMeetingByUser(String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<MeetingDto> result = meetingDao.getMeetingByUser(transaction,
					id);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void editMeeting(MeetingDto meeting) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			meetingDao.editMeeting(transaction, meeting);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteMeeting(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			meetingDao.deleteMeeting(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public MeetingDto getById(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			MeetingDto meeting = meetingDao.getById(transaction, seq);
			transaction.commit();
			return meeting;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public int getSeq() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			int seq = meetingDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public List<MeetingDto> getMeetingToday() throws IOException {
		try {
			transaction.begin();
			List<MeetingDto> result = meetingDao.getMeetingToday(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<MeetingDto> getMeetingToday(String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<MeetingDto> result = meetingDao.getMeetingToday(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<MeetingDto> getMeetingByDayAndUser(String userId, Date date)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<MeetingDto> result = meetingDao.getMeetingByDayAndUser(transaction, userId, date);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
