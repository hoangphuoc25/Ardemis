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
import rd.spec.dao.CategoryDao;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;

public class CategoryDaoImpl implements CategoryDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ProductDao prodDao;

	@Inject
	public CategoryDaoImpl(ProductDao prodDao) {
		this.prodDao = prodDao;
	}

	public List<ProductDto> getProductByCategory(Transaction transaction, String cat) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PRODUCT_BY_CATEGORY);
			prepareStatement.setString(1, cat);
			resultSet = prepareStatement.executeQuery();

			List<ProductDto> result = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				result.add(prodDao.getProductById(transaction, resultSet.getInt(1)));
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

	public void addCategory(Transaction transaction, String category) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_CATEGORY);
			prepareStatement.setString(1, category);
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

	public void addProductCategory(Transaction transaction, ProductDto product) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_PRODUCT_CATEGORY);
			prepareStatement.setInt(1, product.getSeq());

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

	public void deleteProductCategory(Transaction transaction, ProductDto prod) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_PRODUCT_CATEGORY);
			prepareStatement.setInt(1, prod.getSeq());
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
				result.add(makeCategoryDto(transaction, resultSet));
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

	public List<CategoryDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<CategoryDto> result = new ArrayList<CategoryDto>();
			while (resultSet.next()) {
				result.add(makeCategoryDto(transaction, resultSet));
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

	private CategoryDto makeCategoryDto(Transaction transaction, ResultSet resultSet) throws SQLException {
		return new CategoryDto(resultSet.getString(1));
	}

	private static String GET_ALL 					= "select category from t_category";
	private static String GET_CATEGORY_BY_PRODUCT 	= "select category from t_prod_category where product_seq=?";
	private static String GET_PRODUCT_BY_CATEGORY 	= "select distinct product_seq from t_product_category where category=?";
	private static String ADD_CATEGORY 				= "insert into t_category (category) values (?)";
	private static String ADD_PRODUCT_CATEGORY 		= "insert into t_product_category (product_seq, category) values (?, ?)";
	private static String DELETE_PRODUCT_CATEGORY 	= "delete from t_product_cateogry where product_seq=?";
	private static String SEARCH_CATEGORY = "select category from t_category where lower(category) like ?";

	public List<CategoryDto> searchCategory(Transaction transaction, String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_CATEGORY);
			prepareStatement.setString(1, "%" + name + "%");
			resultSet = prepareStatement.executeQuery();

			List<CategoryDto> temp = new ArrayList<CategoryDto>();
			while (resultSet.next()) {
				temp.add(makeCategoryDto(transaction, resultSet));
			}
			return temp;

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
