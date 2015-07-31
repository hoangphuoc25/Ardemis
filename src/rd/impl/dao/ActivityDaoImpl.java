package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ActivityDto;
import rd.dto.CompanyDto;
import rd.dto.UserDto;
import rd.spec.dao.ActivityDao;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class ActivityDaoImpl implements ActivityDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private CompanyDao compDao;
	private UserDao userDao;

	@Inject
	public ActivityDaoImpl(CompanyDao compDao, UserDao userDao) {
		this.compDao = compDao;
		this.userDao = userDao;
	}

	private static String ADD_ACTIVITY = "insert into t_activity (seq, customer_seq, start_date, status, remark, salesperson) values (?, ?, ?, ?, ?, ?)";
	private static String GET_BY_ID = "select seq, customer_seq, start_date, status, remark, salesperson from t_activity where seq=?";
	private static String GET_SEQ = "select max(seq) + 1 from t_activity";
	private static String GET_BY_USER = "select seq, customer_seq, start_date, status, remark, salesperson from t_activity where salesperson=?";
	private static String DELETE_ACTIVITY = "delete from t_activity where seq=?";
	private static String UPDATE_ACTIVITY = "update t_activity set customer_seq=?, start_date=?, status=?, remark=?, salesperson=? where seq=?";
	private static String FIND_BY_STATUS = "select seq, customer_seq, start_date, status, remark, salesperson from t_activity where salesperson=? and lower(status)=?";

	public void addActivity(Transaction transaction, ActivityDto act)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_ACTIVITY);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, act.getCustomer().getSeq());
			prepareStatement.setDate(3, new java.sql.Date(act.getStartDate()
					.getTime()));
			prepareStatement.setString(4, act.getStatus());
			prepareStatement.setString(5, act.getRemark());
			prepareStatement.setString(6, act.getSalesperson().getId());

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

	public ActivityDto getById(Transaction transaction, int seq)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			ActivityDto result = null;
			while (resultSet.next()) {
				result = makeActivityDto(transaction, resultSet);
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

	public List<ActivityDto> getByUser(Transaction transaction, String username)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_USER);
			prepareStatement.setString(1, username);
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				result.add(makeActivityDto(transaction, resultSet));
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

	public void updateActivity(Transaction transaction, ActivityDto act)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_ACTIVITY);
			prepareStatement.setInt(1, act.getCustomer().getSeq());
			prepareStatement.setDate(2, new java.sql.Date(act.getStartDate()
					.getTime()));
			prepareStatement.setString(3, act.getStatus());
			prepareStatement.setString(4, act.getRemark());
			prepareStatement.setString(5, act.getSalesperson().getId());
			prepareStatement.setInt(6, act.getSeq());

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

	public void deleteActivity(Transaction transaction, ActivityDto act)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_ACTIVITY);
			prepareStatement.setInt(1, act.getSeq());
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

	private ActivityDto makeActivityDto(Transaction transaction,
			ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		CompanyDto comp = compDao.getById(transaction, resultSet.getInt(2));
		Date startDate = new Date(resultSet.getDate(3).getTime());
		String status = resultSet.getString(4);
		String remark = resultSet.getString(5);
		UserDto salesperson = userDao.findUser(transaction,
				resultSet.getString(6));
		return new ActivityDto(seq, comp, startDate, status, remark,
				salesperson);
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
	public List<ActivityDto> findByStatus(Transaction transaction, String status, String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(FIND_BY_STATUS);
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, status);
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				result.add(makeActivityDto(transaction, resultSet));
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
