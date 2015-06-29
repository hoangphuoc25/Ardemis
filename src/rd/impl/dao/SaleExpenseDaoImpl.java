package rd.impl.dao;

import java.io.IOException;
import java.io.Serializable;
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

import rd.dto.RoleDto;
import rd.dto.SaleExpenseDto;
import rd.dto.UserDto;
import rd.spec.dao.SaleExpenseDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class SaleExpenseDaoImpl implements SaleExpenseDao, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserDao userDao;

	@Inject
	public SaleExpenseDaoImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	public void addSaleExpense(Transaction transaction, SaleExpenseDto se) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_SALE_EXPENSE);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, se.getSalesperson().getId());
			prepareStatement.setDate(3, new java.sql.Date(se.getReceiptdate().getTime()));
			prepareStatement.setString(4, se.getPurpose());
			prepareStatement.setString(5, se.getReceiptNo());
			prepareStatement.setDouble(6, se.getAmount());
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
	private static String ADD_SALE_EXPENSE = "insert into t_sale_expense (seq, sale_id, receipt_date, purpose, receipt_no, amount) values (?, ?, ?, ?, ?, ?)";

	public SaleExpenseDto getSaleExpenseById (Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SALE_EXPENSE_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			SaleExpenseDto result = null;
			while (resultSet.next()) {
				result = makeSaleExpenseDto(transaction, resultSet);
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

	private SaleExpenseDto makeSaleExpenseDto(Transaction transaction,
			ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		UserDto salesperson = userDao.findUser(transaction, resultSet.getString(2));
		Date receiptDate = new Date(resultSet.getDate(3).getTime());
		String purpose = resultSet.getString(4);
		String receiptNo = resultSet.getString(5);
		double amount = resultSet.getDouble(6);
		return new SaleExpenseDto(seq, salesperson, receiptDate, purpose, receiptNo, amount);
	}

	private static String GET_SALE_EXPENSE_BY_ID = "select seq, sale_id, receipt_date, purpose, receipt_no, amount from t_sale_expense where seq=?";

	public void updateSaleExpense (Transaction transaction, SaleExpenseDto se) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_SALE_EXPENSE);
			prepareStatement.setString(1, se.getSalesperson().getId());
			prepareStatement.setDate(2, new java.sql.Date(se.getReceiptdate().getTime()));
			prepareStatement.setString(3, se.getPurpose());
			prepareStatement.setString(4, se.getReceiptNo());
			prepareStatement.setDouble(5, se.getAmount());
			prepareStatement.setInt(6, se.getSeq());

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

	private static String UPDATE_SALE_EXPENSE = "update t_note set sale_id=?, receipt_date=?, purpose=?, receipt_no=?, amount=? where seq=?";

	public void deleteSaleExpense (Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_SALE_EXPENSE);
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

	private static String DELETE_SALE_EXPENSE = "delete from t_sale_expense where seq=?";
}
