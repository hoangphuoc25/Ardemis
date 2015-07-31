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
import rd.dto.ScheduleTaskDto;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.ScheduleTaskDao;
import rd.spec.dao.Transaction;

public class ScheduleTaskDaoImpl implements ScheduleTaskDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static String GET_SEQ = "select max(seq)+1 from t_event";
	private static String ADD_EVENT = "insert into t_event (seq, category, customer_seq, time, username, detail) values (?, ?, ?, ?, ?, ?) ";
	private static String UPDATE_EVENT = "update t_event set category=?, customer_seq=?, time=?, username=?, detail=? where seq=?";
	private static String DELETE_EVENT = "delete from t_event where seq=?";
	private static String GET_BY_ID = "select seq, category, customer_seq, time, username, detail from t_event where seq=?";
	private static String GET_BY_USER = "select seq, category, customer_seq, time, username, detail from t_event where username=? order by time asc";
	private static String GET_BY_USER_TODAY = "select seq, category, customer_seq, time, username, detail from t_event where username=? and time>=? and time<?";

	private CompanyDao compDao;

	@Inject
	public ScheduleTaskDaoImpl(CompanyDao compDao) {
		this.compDao = compDao;
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
			prepareStatement.setInt(3, evt.getCustomer().getSeq());
			prepareStatement.setTimestamp(4, new java.sql.Timestamp(evt.getTime().getTime()));
			prepareStatement.setString(5, evt.getUsername());
			prepareStatement.setString(6, evt.getDetail());

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
			prepareStatement.setInt(2, evt.getCustomer().getSeq());
			prepareStatement.setTimestamp(3, new java.sql.Timestamp(evt.getTime().getTime()));
			prepareStatement.setString(4, evt.getUsername());
			prepareStatement.setString(5, evt.getDetail());
			prepareStatement.setInt(6, evt.getSeq());

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
			System.out.println(result.size());
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
		CompanyDto comp = compDao.getById(transaction, resultSet.getInt(3));
		Date time = new Date(resultSet.getTimestamp(4).getTime());
		String username = resultSet.getString(5);
		String detail = resultSet.getString(6);

		return new ScheduleTaskDto(seq, category, comp, time, username, detail);
	}
}
