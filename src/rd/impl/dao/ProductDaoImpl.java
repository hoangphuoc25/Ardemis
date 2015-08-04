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

import rd.dto.CategoryDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;
import rd.spec.dataCache.ProductCache;

public class ProductDaoImpl implements ProductDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ProductCache prodCache;

	@Inject
	public ProductDaoImpl(ProductCache prodCache) {
		this.prodCache = prodCache;
	}

	private static String GET_ALL 					= "select seq, name, summary, target, price from t_product order by seq";
	private static String GET_SEQ  					= "select max(seq)+1 from t_product";
	private static String ADD_PRODUCT 				= "insert into t_product (seq, name, summary, target, price) values (?, ?, ?, ?, ?)";
	private static String GET_PRODUCT_BY_ID 		= "select seq, name, summary, target, price from t_product where seq=?";
	private static String REMOVE_PRODUCT 			= "delete from t_product where seq=?";
	private static String UPDATE_PRODUCT 			= "update t_product set name=?, summary=?, target=?, price=? where seq=?";
	private static String SEARCH_BY_NAME 			= "select seq, name, summary, target, price from t_product where upper(name) like ?";
	private static String GET_BY_NAME 				= "select seq, name, summary, target, price from t_product where name=?";
	private static String GET_CATEGORY_BY_PRODUCT 	= "select category from t_product_cateogry where product_seq=?";

	private static String ADD_PRODUCT_CATEGORY 		= "insert into t_prod_category (product_seq, category) values (?, ?)";
	private static String REMOVE_PRODUCT_CATEGORY 	= "delete from t_prod_category where product_seq=?";
	private static String SEARCH_BY_PRODUCT_DESC 	= "select seq, name, summary, target, price from t_product where summary like ?";


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
			prodCache.putProduct(product);

			for (CategoryDto cat: product.getCategory()) {
				prepareStatement = connection.prepareStatement(ADD_PRODUCT_CATEGORY);
				prepareStatement.setInt(1, getSeq(transaction));
				prepareStatement.setString(2, cat.getCategory());
				resultSet = prepareStatement.executeQuery();
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

	public int getSeq(Transaction transaction) throws IOException {
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

	public void removeProduct(Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(REMOVE_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			prodCache.removeProduct(seq);

			prepareStatement = connection.prepareStatement(REMOVE_PRODUCT_CATEGORY);
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

	public ProductDto getProductById(Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE

		ProductDto temp = prodCache.getProduct(seq);
		if (temp != null) {
			return temp;
		}

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PRODUCT_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			ProductDto result = null;
			while (resultSet.next()) {
				result = makeProductDto(transaction, resultSet);
				prodCache.putProduct(result);
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

	private ProductDto makeProductDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		String name = resultSet.getString(2);
		String summary = resultSet.getString(3);
		String target = resultSet.getString(4);
		double price = resultSet.getDouble(5);
		return new ProductDto(seq, name, summary, target, price, null);
	}

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

			prodCache.putProduct(product);

			prepareStatement = connection.prepareStatement(REMOVE_PRODUCT_CATEGORY);
			prepareStatement.setInt(1, product.getSeq());
			resultSet = prepareStatement.executeQuery();

			for (CategoryDto dto: product.getCategory()) {
				prepareStatement = connection.prepareStatement(ADD_PRODUCT_CATEGORY);
				prepareStatement.setInt(1, product.getSeq());
				prepareStatement.setString(2, dto.getCategory());
				resultSet = prepareStatement.executeQuery();
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

	public List<ProductDto> searchByName(Transaction transaction, String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_BY_NAME);
			prepareStatement.setString(1, "%" + name.toUpperCase() + "%");
			resultSet = prepareStatement.executeQuery();

			List<ProductDto> result = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				ProductDto temp = makeProductDto(transaction, resultSet);
				result.add(temp);
				prodCache.putProduct(temp);
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

	public List<ProductDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE

		prodCache.clear();

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<ProductDto> all = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				ProductDto temp = makeProductDto(transaction, resultSet);
				all.add(temp);
				prodCache.putProduct(temp);
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

	public ProductDto getByName(Transaction transaction, String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_NAME);
			prepareStatement.setString(1, name);
			resultSet = prepareStatement.executeQuery();

			ProductDto result = null;
			while (resultSet.next()) {
				result = makeProductDto(transaction, resultSet);
				prodCache.putProduct(result);
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
	public List<CategoryDto> getCategoryByProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_CATEGORY_BY_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<CategoryDto> result = new ArrayList<CategoryDto>();
			while (resultSet.next()) {
				result.add(new CategoryDto(resultSet.getString(1)));
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

	public List<ProductDto> searchByCategories(Transaction transaction, List<String> categories) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {

			StringBuilder builder = new StringBuilder();
			for( int i = 0 ; i < categories.size(); i++ ) {
			    builder.append("?, ");
			}
			String stmt = "SELECT p.product_seq from t_prod_category p WHERE p.category IN ("
		               + builder.deleteCharAt(builder.length() - 2).toString() + ") GROUP BY p.product_seq HAVING COUNT(DISTINCT p.category) = ?";
			System.out.println(stmt);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(stmt);
			int i = 1;
			for (String cat: categories) {
				prepareStatement.setString(i, cat);
				i++;
			}
			prepareStatement.setInt(i, categories.size());

			resultSet = prepareStatement.executeQuery();

			System.out.println(prepareStatement);
			List<ProductDto> result = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				System.out.println(resultSet.getInt(1));
				result.add(getProductById(transaction, resultSet.getInt(1)));
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
	private static String SEARCH_BY_CATEGORIES = "SELECT p.product_seq from t_prod_category p WHERE p.category IN (?) GROUP BY p.product_seq HAVING COUNT(DISTINCT p.category) = ?";
	public List<ProductDto> searchByProductDesc(Transaction transaction, String keyword) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_BY_PRODUCT_DESC);
			prepareStatement.setString(1, "%" + keyword.toLowerCase() + "%");
			resultSet = prepareStatement.executeQuery();

			List<ProductDto> result = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				result.add(makeProductDto(transaction, resultSet));
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
