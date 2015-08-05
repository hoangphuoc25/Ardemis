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

import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.MeetingDto;
import rd.dto.UserDto;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.ContactDao;
import rd.spec.dao.MeetingDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class MeetingDaoImpl implements MeetingDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject UserDao userDao;
	@Inject CompanyDao compDao;
	@Inject ContactDao contactDao;

	public void addMeeting(Transaction transaction, MeetingDto meeting) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_MEETING);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, meeting.getSalesperson().getId());
			prepareStatement.setTimestamp(3, new java.sql.Timestamp(meeting.getFrom().getTime()));
			prepareStatement.setTimestamp(4, new java.sql.Timestamp(meeting.getTo().getTime()));
			prepareStatement.setString(5, meeting.getDetail());
			prepareStatement.setString(6, meeting.getLocation());
			prepareStatement.setInt(7, meeting.getContact().getSeq());

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

	public List<MeetingDto> getMeetingByUser(Transaction transaction, String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_MEETING_BY_USER);
			prepareStatement.setString(1, id);
			resultSet = prepareStatement.executeQuery();

			List<MeetingDto> result = new ArrayList<MeetingDto>();
			while (resultSet.next()) {
				result.add(makeMeetingDto(transaction, resultSet));
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

	public void editMeeting(Transaction transaction, MeetingDto meeting) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(EDIT_MEETING);
			prepareStatement.setString(1, meeting.getSalesperson().getId());
			prepareStatement.setTimestamp(2, new java.sql.Timestamp(meeting.getFrom().getTime()));
			prepareStatement.setTimestamp(3, new java.sql.Timestamp(meeting.getTo().getTime()));
			prepareStatement.setString(4, meeting.getDetail());
			prepareStatement.setString(5, meeting.getLocation());
			prepareStatement.setInt(6, meeting.getContact().getSeq());
			prepareStatement.setInt(7, meeting.getSeq());
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

	public void deleteMeeting(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_MEETING);
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

	public MeetingDto getById(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			MeetingDto result = null;
			while (resultSet.next()) {
				result = makeMeetingDto(transaction, resultSet);
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

	public int getSeq(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int result = 1;
			while (resultSet.next()) {
				result = resultSet.getInt(1);
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

	private MeetingDto makeMeetingDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		UserDto user = userDao.findUser(transaction, resultSet.getString(2));
		java.util.Date from = new java.util.Date(resultSet.getTimestamp(3).getTime());
		java.util.Date to = new java.util.Date(resultSet.getTimestamp(4).getTime());
		String detail = resultSet.getString(5);
		String location = resultSet.getString(6);
		ContactDto contact = contactDao.getContactById(transaction, resultSet.getInt(7));

		return new MeetingDto(seq, from, to, detail, contact, user, location);
	}
	public List<MeetingDto> getMeetingToday(Transaction transaction) throws IOException {
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
			prepareStatement = connection.prepareStatement(GET_MEETING_TODAY);
			prepareStatement.setDate(1, new java.sql.Date(date.getTime().getTime()));
			prepareStatement.setDate(2, new java.sql.Date(nextDay.getTime().getTime()));

			resultSet = prepareStatement.executeQuery();

			List<MeetingDto> result = new ArrayList<MeetingDto>();
			while (resultSet.next()) {
				result.add(makeMeetingDto(transaction, resultSet));
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

	public List<MeetingDto> getMeetingToday(Transaction transaction, String username) throws IOException {
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
			prepareStatement = connection.prepareStatement(GET_MEETING_TODAY_USER);
			prepareStatement.setDate(1, new java.sql.Date(date.getTime().getTime()));
			prepareStatement.setDate(2, new java.sql.Date(nextDay.getTime().getTime()));
			prepareStatement.setString(3, username);

			resultSet = prepareStatement.executeQuery();

			List<MeetingDto> result = new ArrayList<MeetingDto>();
			while (resultSet.next()) {
				result.add(makeMeetingDto(transaction, resultSet));
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

	public List<MeetingDto> getMeetingByDayAndUser(Transaction transaction, String userId, Date date) throws IOException {
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
			nextDay.set(Calendar.HOUR_OF_DAY, 0);
			nextDay.set(Calendar.MINUTE, 0);
			nextDay.set(Calendar.SECOND, 0);
			nextDay.set(Calendar.MILLISECOND, 0);
			nextDay.add(Calendar.DAY_OF_MONTH, 1);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_MEETING_BY_DAY_AND_USER);
			prepareStatement.setDate(1, new java.sql.Date(day.getTime().getTime()));
			prepareStatement.setDate(2, new java.sql.Date(nextDay.getTime().getTime()));
			prepareStatement.setString(3, userId);

			resultSet = prepareStatement.executeQuery();

			List<MeetingDto> result = new ArrayList<MeetingDto>();
			while (resultSet.next()) {
				result.add(makeMeetingDto(transaction, resultSet));
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
	private static String GET_MEETING_BY_DAY_AND_USER = "select seq, user_id, from_date, to_date, detail, location, contact_seq from t_meeting where from_date >= ? and from_date < ? and user_id=?";
	private static String ADD_MEETING 			 = "insert into t_meeting (seq, user_id, from_date, to_date, detail, location, contact_seq) values (?, ?, ?, ?, ?, ?, ?)";
	private static String GET_MEETING_BY_USER 	 = "select seq, user_id, from_date, to_date, detail, location, contact_seq from t_meeting where user_id=? order by from_date desc";
	private static String EDIT_MEETING 			 = "update t_meeting set user_id=?, from_date=?, to_date=?, detail=?, location=?, contact_seq=? where seq=?";
	private static String DELETE_MEETING 		 = "delete from t_meeting where seq=?";
	private static String GET_BY_ID 			 = "select seq, user_id, from_date, to_date, detail, location, contact_seq from t_meeting where seq=?";
	private static String GET_SEQ 				 = "select max(seq)+1 from t_meeting";
	private static String GET_MEETING_TODAY 	 = "select seq, user_id, from_date, to_date, detail, location, contact_seq from t_meeting where from_date >= ? and from_date < ? order by from_date asc";
	private static String GET_MEETING_TODAY_USER = "select seq, user_id, from_date, to_date, detail, location, contact_seq from t_meeting where from_date >= ? and from_date < ? and user_id=?";
}
