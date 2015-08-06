package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ExpBudgetDto;
import rd.dto.UserDto;
import rd.spec.dao.ExpBudgetDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class ExpBudgetDaoImpl implements ExpBudgetDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserDao userDao;


	private static String ADD_BUDGET = "insert into t_expense_budget (salesperson, budget, from_date, to_date) values (?, ?, ?, ?)";
	private static String UPDATE_BUDGET = "update t_expense_budget set budget=? where salesperson=? and from_date=? and to_date=?";
	private static String DELETE_BUDGET = "delete from t_expense_budget where salesperson=?";
	private static String GET_CURRENT_BUDGET = "select salesperson, budget, from_date, to_date where salesperson=? and from_date<=? and end_date>=?";
	private static String GET_BUDGET_BY_USER = "select salesperson, budget, from_date, to_date where salesperson=?";

	public void addBudget(Transaction transaction, ExpBudgetDto budget) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_BUDGET);
			prepareStatement.setString(1, budget.getUser().getId());
			prepareStatement.setInt(2, budget.getBudget());
			prepareStatement.setDate(3, new java.sql.Date(budget.getFrom().getTime()));
			prepareStatement.setDate(4, new java.sql.Date(budget.getTo().getTime()));

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

	public void updateBudget(Transaction transaction, ExpBudgetDto budget) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_BUDGET);
			prepareStatement.setInt(1, budget.getBudget());
			prepareStatement.setString(2, budget.getUser().getId());
			prepareStatement.setDate(3, new java.sql.Date(budget.getFrom().getTime()));
			prepareStatement.setDate(4, new java.sql.Date(budget.getTo().getTime()));

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

	public void deleteBudget(Transaction transaction, ExpBudgetDto budget) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_BUDGET);
			prepareStatement.setString(1, budget.getUser().getId());
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

	public List<ExpBudgetDto> getBudgetByUser(Transaction transaction, String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BUDGET_BY_USER);
			prepareStatement.setString(1, userId);
			resultSet = prepareStatement.executeQuery();

			List<ExpBudgetDto> result = new ArrayList<ExpBudgetDto>();
			while (resultSet.next()) {
				result.add(makeBudgetDto(transaction, resultSet));
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

	public ExpBudgetDto getCurrentBudget(Transaction transaction, String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_CURRENT_BUDGET);
			prepareStatement.setString(1, userId);
			prepareStatement.setDate(2, new java.sql.Date((new Date()).getTime()));
			prepareStatement.setDate(3, new java.sql.Date((new Date()).getTime()));
			resultSet = prepareStatement.executeQuery();

			ExpBudgetDto result = new ExpBudgetDto();
			if (resultSet.next()) {
				result = makeBudgetDto(transaction, resultSet);
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

	private ExpBudgetDto makeBudgetDto(Transaction transaction, ResultSet resultSet) throws IOException, SQLException {
		UserDto user = userDao.findUser(transaction, resultSet.getString(1));
		int budget = resultSet.getInt(2);
		Date from = new Date(resultSet.getDate(3).getTime());
		Date to = new Date(resultSet.getDate(4).getTime());

		return new ExpBudgetDto(user, budget, from, to);
	}
}
