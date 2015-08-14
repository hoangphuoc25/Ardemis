package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ContactDto;
import rd.dto.ScheduleTaskDto;
import rd.spec.dao.ContactDao;
import rd.spec.dao.ScheduleTaskDao;
import rd.spec.dao.Transaction;

public class ScheduleTaskDaoImpl implements ScheduleTaskDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static String GET_SEQ 		= "select max(seq)+1 from t_event";
	private static String ADD_EVENT		= "insert into t_event (seq, category, contact_seq, time, username, detail, activity_seq, status) values (?, ?, ?, ?, ?, ?, ?, ?) ";
	private static String UPDATE_EVENT 	= "update t_event set category=?, contact_seq=?, time=?, username=?, detail=?, activity_seq=?, status=? where seq=?";
	private static String DELETE_EVENT 	= "delete from t_event where seq=?";
	private static String GET_BY_ID 	= "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where seq=?";
	private static String GET_BY_USER 	= "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where username=? order by time asc";
	private static String GET_BY_USER_TODAY = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where username=? and time>=? and time<? order by time asc";
	private static String GET_BY_COMPANY = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where contact_seq=?";
	private static String GET_BY_CONTACT = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where contact_seq=?";
	private static String GET_BY_DEAL 	 = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where activity_seq=?";
	private static String GET_TASK_NEXT_WEEK_BY_USER = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where username=? and time>=? and time<? order by time asc";
	private static String GET_BY_USER_AND_DATE = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where username=? and time>=? and time<? order by time asc";
	private static String GET_BY_USER_AND_STATUS = "select seq, category, contact_seq, time, username, detail, activity_seq, status from t_event where username=? and status=?";

	private ContactDao contactDao;

	@Inject
	public ScheduleTaskDaoImpl(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

	public int getSeq(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int seq = 1;
			while (resultSet.next()) {
				seq = resultSet.getInt(1);
			}
			return seq;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public void addEvent(Transaction transaction, ScheduleTaskDto evt)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_EVENT);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, evt.getCategory());
			prepareStatement.setInt(3, evt.getContact().getSeq());
			prepareStatement.setTimestamp(4, new java.sql.Timestamp(evt.getTime().getTime()));
			prepareStatement.setString(5, evt.getUsername());
			prepareStatement.setString(6, evt.getDetail());
			prepareStatement.setInt(7, evt.getActId());
			prepareStatement.setString(8, evt.getStatus());

			resultSet = prepareStatement.executeQuery();

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public void updateEvent(Transaction transaction, ScheduleTaskDto evt) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_EVENT);
			prepareStatement.setString(1, evt.getCategory());
			prepareStatement.setInt(2, evt.getContact().getSeq());
			prepareStatement.setTimestamp(3, new java.sql.Timestamp(evt.getTime().getTime()));
			prepareStatement.setString(4, evt.getUsername());
			prepareStatement.setString(5, evt.getDetail());
			prepareStatement.setInt(6, evt.getActId());
			prepareStatement.setString(7, evt.getStatus());
			prepareStatement.setInt(8, evt.getSeq());

			resultSet = prepareStatement.executeQuery();

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public void deleteEvent(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_EVENT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public ScheduleTaskDto getById(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			ScheduleTaskDto result = new ScheduleTaskDto();
			while (resultSet.next()) {
				result = makeScheduleTaskDto(transaction, resultSet);
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public List<ScheduleTaskDto> getByUser(Transaction transaction, String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_USER);
			prepareStatement.setString(1, username);
			resultSet = prepareStatement.executeQuery();

			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public List<ScheduleTaskDto> getByUserToday(Transaction transaction, String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Calendar date = new GregorianCalendar();
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);

			Calendar nextDay = new GregorianCalendar();
			nextDay.set(Calendar.HOUR_OF_DAY, 0);
			nextDay.set(Calendar.MINUTE, 0);
			nextDay.set(Calendar.SECOND, 0);
			nextDay.set(Calendar.MILLISECOND, 0);
			nextDay.add(Calendar.DAY_OF_MONTH, 1);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_USER_TODAY);
			prepareStatement.setString(1, username);
			prepareStatement.setDate(2, new java.sql.Date(date.getTime().getTime()));
			prepareStatement.setDate(3, new java.sql.Date(nextDay.getTime().getTime()));

			resultSet = prepareStatement.executeQuery();

			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	private ScheduleTaskDto makeScheduleTaskDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		String category = resultSet.getString(2);
		// CompanyDto comp = compDao.getById(transaction, resultSet.getInt(3));
		ContactDto contact = contactDao.getContactById(transaction, resultSet.getInt(3));
		Date time = new Date(resultSet.getTimestamp(4).getTime());
		String username = resultSet.getString(5);
		String detail = resultSet.getString(6);
		int actId = resultSet.getInt(7);
		String status = resultSet.getString(8);

		return new ScheduleTaskDto(seq, category, time, username, detail, contact, actId, status);
	}
	public List<ScheduleTaskDto> getByCompany(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_COMPANY);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public ContactDao getContactDao() {
		return contactDao;
	}

	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}
	public List<ScheduleTaskDto> getByContact(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_CONTACT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();


			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}

	public List<ScheduleTaskDto> getByDeal(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_DEAL);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();


			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	public List<ScheduleTaskDto> getTaskNextWeekByUser(Transaction transaction, String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Calendar date = new GregorianCalendar();
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);

			Calendar nextDay = new GregorianCalendar();
			nextDay.set(Calendar.HOUR_OF_DAY, 0);
			nextDay.set(Calendar.MINUTE, 0);
			nextDay.set(Calendar.SECOND, 0);
			nextDay.set(Calendar.MILLISECOND, 0);
			nextDay.add(Calendar.DAY_OF_MONTH, 7);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_TASK_NEXT_WEEK_BY_USER);
			prepareStatement.setString(1, userId);
			prepareStatement.setDate(2, new java.sql.Date(date.getTime().getTime()));
			prepareStatement.setDate(3, new java.sql.Date(nextDay.getTime().getTime()));

			resultSet = prepareStatement.executeQuery();

			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	public List<ScheduleTaskDto> getByUser(Transaction transaction, String userId, Date date) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Calendar day = new GregorianCalendar();
			day.setTime(date);
			day.set(Calendar.HOUR_OF_DAY, 0);
			day.set(Calendar.MINUTE, 0);
			day.set(Calendar.SECOND, 0);
			day.set(Calendar.MILLISECOND, 0);

			Calendar nextDay = new GregorianCalendar();
			nextDay.setTime(date);
			nextDay.set(Calendar.HOUR_OF_DAY, 0);
			nextDay.set(Calendar.MINUTE, 0);
			nextDay.set(Calendar.SECOND, 0);
			nextDay.set(Calendar.MILLISECOND, 0);
			nextDay.add(Calendar.DAY_OF_MONTH, 1);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_TASK_NEXT_WEEK_BY_USER);
			prepareStatement.setString(1, userId);
			prepareStatement.setDate(2, new java.sql.Date(day.getTime().getTime()));
			prepareStatement.setDate(3, new java.sql.Date(nextDay.getTime().getTime()));

			resultSet = prepareStatement.executeQuery();

			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
	public List<ScheduleTaskDto> getByUserAndStatus(Transaction transaction, String userId, String status) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_USER_AND_STATUS);
			prepareStatement.setString(1, userId);
			prepareStatement.setString(2, status);
			resultSet = prepareStatement.executeQuery();

			List<ScheduleTaskDto> result = new ArrayList<ScheduleTaskDto>();
			while (resultSet.next()) {
				result.add(makeScheduleTaskDto(transaction, resultSet));
			}
			return result;

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
	}
}
