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

import rd.dto.SaleTargetDto;
import rd.dto.UserDto;
import rd.spec.dao.SaleTargetDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class SaleTargetDaoImpl implements SaleTargetDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserDao userDao;

	@Inject
	public SaleTargetDaoImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	private static String ADD_SALE_TARGET 		= "insert into t_sale_target (sale, target, from_date, to_date, current_record, unit, action) values (?, ?, ?, ?, ?, ?, ?)";
	private static String DELETE_SALE_TARGET 	= "delete from t_sale_target where sale=?";
	private static String GET_SEQ 				= "select max(seq) + 1 from t_sale_target";
	private static String UPDATE_SALE_TARGET 	= "update t_sale_target set target=?, from_date=?, to_date=?, current_record=?, unit=?, action=? where sale=?";
	private static String GET_BY_USER 			= "select seq, target, from_date, to_date, current_record, unit, action from t_sale where sale=?";
	private static String GET_SALE_TARGET 		= "select sale, target, from_date, to_date, current_record, unit, action from t_sale_target where sale=?";

	public SaleTargetDto getSaleTarget(Transaction transaction, String saleId)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SALE_TARGET);
			prepareStatement.setString(1, saleId);
			resultSet = prepareStatement.executeQuery();

			SaleTargetDto std = new SaleTargetDto();
			if (resultSet.next()) {
				std = makeSaleTargetDto(transaction, resultSet);
			}
			return std;
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

	private SaleTargetDto makeSaleTargetDto(Transaction transaction,
			ResultSet resultSet) throws IOException, SQLException {
		UserDto sale = userDao.findUser(transaction, resultSet.getString(1));
		int target = resultSet.getInt(2);
		Date fromDate = new Date(resultSet.getDate(3).getTime());
		Date toDate = new Date(resultSet.getDate(4).getTime());
		int current = resultSet.getInt(5);
		String unit = resultSet.getString(6);
		String action = resultSet.getString(7);

		return new SaleTargetDto(sale, action, target, fromDate, toDate, current, unit);
	}

	public void addSaleTarget(Transaction transaction, SaleTargetDto std)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			// int current = (getByUser(transaction, std.getSale().getId())).getCurrent();

			prepareStatement = connection.prepareStatement(DELETE_SALE_TARGET);
			prepareStatement.setString(1, std.getSale().getId());
			resultSet = prepareStatement.executeQuery();

			prepareStatement = connection.prepareStatement(ADD_SALE_TARGET);
			prepareStatement.setString(1, std.getSale().getId());
			prepareStatement.setInt(2, std.getTarget());
			prepareStatement.setDate(3, new java.sql.Date(std.getFromDate().getTime()));
			prepareStatement.setDate(4, new java.sql.Date(std.getToDate().getTime()));
			prepareStatement.setInt(5, std.getCurrent());
			prepareStatement.setString(6, std.getUnit());
			prepareStatement.setString(7, std.getAction());

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

			return resultSet.getInt(1);

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

	public void updateSaleTarget(Transaction transaction, SaleTargetDto std)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_SALE_TARGET);
			prepareStatement.setInt(1, std.getTarget());
			prepareStatement.setDate(2, new java.sql.Date(std.getFromDate().getTime()));
			prepareStatement.setDate(3, new java.sql.Date(std.getToDate().getTime()));
			prepareStatement.setInt(4, std.getCurrent());
			prepareStatement.setString(5, std.getUnit());
			prepareStatement.setString(6, std.getAction());
			prepareStatement.setString(7, std.getSale().getId());

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

	private SaleTargetDto getByUser(Transaction transaction, String user)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_USER);
			prepareStatement.setString(1, user);
			resultSet = prepareStatement.executeQuery();

			SaleTargetDto std = null;
			if (resultSet.next()) {
				std = makeSaleTargetDto(transaction, resultSet);
			}

			return std;
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
