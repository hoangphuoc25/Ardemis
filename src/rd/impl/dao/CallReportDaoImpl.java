package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CallReportDto;
import rd.dto.ContactDto;
import rd.spec.dao.CallReportDao;
import rd.spec.dao.ContactDao;
import rd.spec.dao.Transaction;

public class CallReportDaoImpl implements CallReportDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ContactDao contactDao;

	@Inject
	public CallReportDaoImpl(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

	public void addReport(Transaction transaction, CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_REPORT);
			prepareStatement.setInt(1, getSeq(transaction));

			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
			}

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

	private int getSeq(Transaction transaction) throws IOException {
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

	private static String GET_SEQ = "select max(seq)+1 from t_call_report";
	private static String ADD_REPORT = "insert into t_call_report (seq, contact_seq, phone, call_date, note, rate) values (?, ?, ?, ?, ?, ?)";

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
		String phone = resultSet.getString(3);
		Date callDate = new Date(resultSet.getDate(4).getTime());
		String note = resultSet.getString(5);
		String rate = resultSet.getString(6);
		return new CallReportDto(seq, contact, phone, callDate, note, rate);
	}

	private static String GET_BY_ID = "";

	public void updateCallReport(Transaction transaction, CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_CALL_REPORT);
			prepareStatement.setInt(1, report.getContact().getSeq());
			prepareStatement.setString(2, report.getPhone());
			prepareStatement.setDate(3, new java.sql.Date(report.getCallDate().getTime()));
			prepareStatement.setString(4, report.getNote());
			prepareStatement.setString(5, report.getRate());
			prepareStatement.setInt(6, report.getSeq());

			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
			}

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

	private static String UPDATE_CALL_REPORT = "update t_call_report set contact_seq=?, phone=?, call_date=?, note=?, rate=? where seq=?";

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

	private static String DELETE_CALL_REPORT = "delete from t_call_report where seq=?";
}
