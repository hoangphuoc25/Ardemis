package rd.impl.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ProductDto;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;

public class ProductDaoImpl implements ProductDao, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void addProduct (Transaction transaction, ProductDto product) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_PRODUCT);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, product.getName());
			prepareStatement.setString(3, product.getSummary());
			prepareStatement.setString(4, product.getTarget());
			prepareStatement.setDouble(5, product.getPrice());

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

			int seq = 1;
			if(resultSet.next()) {
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

	private static String GET_SEQ  = "select max(seq)+1 from t_product";
	private static String ADD_PRODUCT = "insert into t_product (seq, name, summary, description, target, price) values (?, ?, ?, ?, ?, ?)";

	public void removeProduct(Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(REMOVE_PRODUCT);
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

	private static String REMOVE_PRODUCT = "delete from t_product where seq=?";

	public ProductDto getProductById(Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PRODUCT_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			ProductDto result = null;
			while (resultSet.next()) {
				result = makeProductDto(resultSet);
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

	private ProductDto makeProductDto(ResultSet resultSet) throws SQLException {
		int seq = resultSet.getInt(1);
		String name = resultSet.getString(2);
		String summary = resultSet.getString(3);
		String target = resultSet.getString(4);
		double price = resultSet.getDouble(5);
		return new ProductDto(seq, name, summary, target, price);
	}

	private static String GET_PRODUCT_BY_ID = "select seq, name, summary, target, price from t_product where seq=?";

	public void updateProduct (Transaction transaction, ProductDto product) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_PRODUCT);
			prepareStatement.setString(1, product.getName());
			prepareStatement.setString(2, product.getSummary());
			prepareStatement.setString(3, product.getTarget());
			prepareStatement.setDouble(4, product.getPrice());
			prepareStatement.setInt(5, product.getSeq());

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

	private static String UPDATE_PRODUCT = "update t_product set name=?, summary=?, target=?, price=? where seq=?";
}
