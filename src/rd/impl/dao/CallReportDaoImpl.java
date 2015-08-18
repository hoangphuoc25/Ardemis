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

import rd.dto.CallReportDto;
import rd.dto.ContactDto;
import rd.dto.UserDto;
import rd.spec.dao.CallReportDao;
import rd.spec.dao.ContactDao;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class CallReportDaoImpl implements CallReportDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ContactDao contactDao;
	private UserDao userDao;
	private ProductDao productDao;

	@Inject
	public CallReportDaoImpl(ContactDao contactDao, UserDao userDao, ProductDao productDao) {
		this.userDao = userDao;
		this.productDao = productDao;
		this.contactDao = contactDao;
	}

	private static String GET_ALL 				= "select seq, contact_seq, call_time, detail, rating, sale_id, call_back, activity_seq from t_call_report order by call_time desc";
	private static String DELETE_CALL_REPORT 	= "delete from t_call_report where seq=?";
	private static String GET_SEQ 				= "select max(seq)+1 from t_call_report";
	private static String ADD_REPORT 			= "insert into t_call_report (seq, contact_seq, call_time, detail, rating, sale_id, call_back, activity_seq) values (?, ?, ?, ?, ?, ?, ?, ?)";
	private static String GET_BY_ID 			= "select seq, contact_seq, call_time, detail, rating, sale_id, call_back, activity_seq from t_call_report where seq=?";
	private static String UPDATE_CALL_REPORT 	= "update t_call_report set contact_seq=?, call_time=?, detail=?, rating=?, sale_id=?, call_back=?, activity_seq=? where seq=?";
	private static String GET_BY_COMPANY_ID 	= "select seq, contact_seq, call_time, detail, rating, sale_id, call_back, activity_seq from t_call_report where contact_seq=? order by call_time desc";
	private static String GET_BY_CONTACT 		= "select seq, contact_seq, call_time, detail, rating, sale_id, call_back, activity_seq from t_call_report where contact_seq=?";
	private static String GET_BY_DEAL 			= "select seq, contact_seq, call_time, detail, rating, sale_id, call_back, activity_seq from t_call_report where activity_seq=?";
	private static String COUNT_REPORT_BY_USER_AND_TIME = "select count(distinct seq) from t_call_report where sale_id=? and call_time>=? and call_time<?";

	public void addReport(Transaction transaction, CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_REPORT);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, report.getContact().getSeq());
			prepareStatement.setTimestamp(3, new java.sql.Timestamp(report.getCallTime().getTime()));
			prepareStatement.setString(4, report.getDetail());
			prepareStatement.setString(5, report.getRating());
			prepareStatement.setString(6, report.getSalesperson().getId());
			prepareStatement.setInt(7, report.getCallBack());
			prepareStatement.setInt(8, report.getActId());

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

	public int getSeq(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int result = 1;
			if (resultSet.next()) {
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

	public CallReportDto getById(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			CallReportDto result = new CallReportDto();
			while (resultSet.next()) {
				result = makeCallReportDto(transaction, resultSet);
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

	private CallReportDto makeCallReportDto(Transaction transaction, ResultSet resultSet) throws IOException, SQLException {
		int seq = resultSet.getInt(1);
		ContactDto contact = contactDao.getContactById(transaction, resultSet.getInt(2));
		java.util.Date callTime = new java.util.Date(resultSet.getTimestamp(3).getTime());
		String detail = resultSet.getString(4);
		String rating = resultSet.getString(5);
		UserDto salesman = userDao.findUser(transaction, resultSet.getString(6));
		int callBack = resultSet.getInt(7);
		int actId = resultSet.getInt(8);

		return new CallReportDto(seq, contact, callTime, detail, rating, salesman, callBack, actId);
	}

	public void updateCallReport(Transaction transaction, CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_CALL_REPORT);
			prepareStatement.setInt(1, report.getContact().getSeq());
			prepareStatement.setTimestamp(2, new java.sql.Timestamp(report.getCallTime().getTime()));
			prepareStatement.setString(3, report.getDetail());
			prepareStatement.setString(4, report.getRating());
			prepareStatement.setString(5, report.getSalesperson().getId());
			prepareStatement.setInt(6, report.getCallBack());
			prepareStatement.setInt(7, report.getActId());
			prepareStatement.setInt(8, report.getSeq());

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

	public void deleteCallReport(Transaction transaction, CallReportDto cr) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_CALL_REPORT);
			prepareStatement.setInt(1, cr.getSeq());
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

	public List<CallReportDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<CallReportDto> all = new ArrayList<CallReportDto>();
			while (resultSet.next()) {
				all.add(makeCallReportDto(transaction, resultSet));
			}
			return all;
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
	public List<CallReportDto> getByCompanyId(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_COMPANY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<CallReportDto> result = new ArrayList<CallReportDto>();
			while (resultSet.next()) {
				result.add(makeCallReportDto(transaction, resultSet));
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
	public List<CallReportDto> getByContact(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_CONTACT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<CallReportDto> result = new ArrayList<CallReportDto>();
			while (resultSet.next()) {
				result.add(makeCallReportDto(transaction, resultSet));
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

	public List<CallReportDto> getByDeal(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_DEAL);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<CallReportDto> result = new ArrayList<CallReportDto>();
			while (resultSet.next()) {
				result.add(makeCallReportDto(transaction, resultSet));
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
	public int countReportByUserAndTime(Transaction transaction, String username, Date startDate, Date endDate) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Calendar day = new GregorianCalendar();
			day.setTime(startDate);
			day.set(Calendar.HOUR_OF_DAY, 0);
			day.set(Calendar.MINUTE, 0);
			day.set(Calendar.SECOND, 0);
			day.set(Calendar.MILLISECOND, 0);

			Calendar nextDay = new GregorianCalendar();
			nextDay.setTime(endDate);
			nextDay.set(Calendar.HOUR_OF_DAY, 0);
			nextDay.set(Calendar.MINUTE, 0);
			nextDay.set(Calendar.SECOND, 0);
			nextDay.set(Calendar.MILLISECOND, 0);
			nextDay.add(Calendar.DAY_OF_MONTH, 1);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(COUNT_REPORT_BY_USER_AND_TIME);
			prepareStatement.setString(1, username);
			prepareStatement.setDate(2, new java.sql.Date(day.getTime().getTime()));
			prepareStatement.setDate(3, new java.sql.Date(nextDay.getTime().getTime()));
			resultSet = prepareStatement.executeQuery();

			int result = 0;
			if (resultSet.next()) {
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
}
