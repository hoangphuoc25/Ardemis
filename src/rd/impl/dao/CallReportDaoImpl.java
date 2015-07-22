package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.CallReportDto;
import rd.dto.CompanyDto;
import rd.dto.UserDto;
import rd.spec.dao.CallReportDao;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class CallReportDaoImpl implements CallReportDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private CompanyDao companyDao;
	private UserDao userDao;

	@Inject
	public CallReportDaoImpl(CompanyDao companyDao, UserDao userDao) {
		this.companyDao = companyDao;
		this.userDao = userDao;
	}

	private static String GET_ALL = "select seq, customer_seq, call_time, detail, rating, sale_id from t_call_report order by call_time desc";
	private static String DELETE_CALL_REPORT = "delete from t_call_report where seq=?";
	private static String GET_SEQ = "select max(seq)+1 from t_call_report";
	private static String ADD_REPORT = "insert into t_call_report (seq, customer_seq, call_time, detail, rating, sale_id) values (?, ?, ?, ?, ?, ?)";
	private static String GET_BY_ID = "select seq, customer_seq, call_time, detail, rating, sale_id from t_call_report where seq=?";
	private static String UPDATE_CALL_REPORT = "update t_call_report set customer_seq=?, call_time=?, detail=?, rating=?, sale_id=? where seq=?";
	private static String GET_BY_COMPANY_ID = "select seq, customer_seq, call_time, detail, rating, sale_id from t_call_report where customer_seq=?";

	public void addReport(Transaction transaction, CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			System.out.println("CallReportDaoImpl.addReport()");
			System.out.println(getSeq(transaction));
			System.out.println(report.getCustomer().getSeq());
			System.out.println(report.getCallTime());
			System.out.println(report.getCallTime().getTime());
			System.out.println((new java.sql.Timestamp(report.getCallTime().getTime())).getTime());
			System.out.println(report.getDetail());
			System.out.println(report.getRating());
			System.out.println(report.getSalesperson().getId());

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_REPORT);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, report.getCustomer().getSeq());
			prepareStatement.setTimestamp(3, new java.sql.Timestamp(report.getCallTime().getTime()));
			prepareStatement.setString(4, report.getDetail());
			prepareStatement.setString(5, report.getRating());
			prepareStatement.setString(6, report.getSalesperson().getId());

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
		CompanyDto comp = companyDao.getById(transaction, resultSet.getInt(2));
		java.util.Date callTime = new java.util.Date(resultSet.getTimestamp(3).getTime());
		String detail = resultSet.getString(4);
		String rating = resultSet.getString(5);
		UserDto salesman = userDao.findUser(transaction, resultSet.getString(6));
		return new CallReportDto(seq, comp, callTime, detail, rating, salesman);
	}

	public void updateCallReport(Transaction transaction, CallReportDto report) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_CALL_REPORT);
			prepareStatement.setInt(1, report.getCustomer().getSeq());
			prepareStatement.setTimestamp(2, new java.sql.Timestamp(report.getCallTime().getTime()));
			prepareStatement.setString(3, report.getDetail());
			prepareStatement.setString(4, report.getRating());
			prepareStatement.setString(5, report.getSalesperson().getId());
			prepareStatement.setInt(6, report.getSeq());

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
}
