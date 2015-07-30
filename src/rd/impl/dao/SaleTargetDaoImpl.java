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

	private static String GET_SALE_TARGET = "select sale, target, from_date, to_date, current_record from t_sale_target where sale=?";

	private SaleTargetDto makeSaleTargetDto(Transaction transaction,
			ResultSet resultSet) throws IOException, SQLException {
		UserDto sale = userDao.findUser(transaction, resultSet.getString(1));
		int target = resultSet.getInt(2);
		Date fromDate = new Date(resultSet.getDate(3).getTime());
		System.out.println(fromDate);
		Date toDate = new Date(resultSet.getDate(4).getTime());
		System.out.println(toDate);
		int current = resultSet.getInt(5);
		return new SaleTargetDto(sale, target, fromDate, toDate, current);
	}

	public void addSaleTarget(Transaction transaction, SaleTargetDto std)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_SALE_TARGET);
			prepareStatement.setString(1, std.getSale().getId());
			resultSet = prepareStatement.executeQuery();

			prepareStatement = connection.prepareStatement(ADD_SALE_TARGET);
			prepareStatement.setString(1, std.getSale().getId());
			prepareStatement.setInt(2, std.getTarget());
			prepareStatement.setDate(3, new java.sql.Date(std.getFromDate()
					.getTime()));
			prepareStatement.setDate(4, new java.sql.Date(std.getToDate()
					.getTime()));
			prepareStatement.setInt(5, std.getCurrent());

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

	private static String ADD_SALE_TARGET = "insert into t_sale_target (sale, target, from_date, to_date, current_record) values (?, ?, ?, ?, ?)";
	private static String DELETE_SALE_TARGET = "delete from t_sale_target where sale=?";
	private static String GET_SEQ = "select max(seq) + 1 from t_sale_target";

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
			prepareStatement.setString(5, std.getSale().getId());

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

	private static String UPDATE_SALE_TARGET = "update t_sale_target set target=?, from_date=?, to_date=?, current_record=? where sale=?";
}
