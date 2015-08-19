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

import rd.dto.ProductDto;
import rd.dto.RelatedProductDto;
import rd.spec.dao.ProductDao;
import rd.spec.dao.RelatedProductDao;
import rd.spec.dao.Transaction;

public class RelatedProductDaoImpl implements RelatedProductDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ProductDao productDao;

	@Inject
	public RelatedProductDaoImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	private static String ADD_RELATED_PRODUCTS = "insert into t_related_product (product_seq, related_product_seq) values (?, ?)";

	public List<ProductDto> getRelatedProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_RELATED_PRODUCT);
			prepareStatement.setInt(1, seq);
			prepareStatement.setInt(2, seq);
			prepareStatement.setInt(3, seq);
			resultSet = prepareStatement.executeQuery();

			List<ProductDto> result = new ArrayList<ProductDto>();
			while (resultSet.next()) {
				result.add(productDao.getProductById(transaction, resultSet.getInt(1)));
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

	private static String GET_RELATED_PRODUCT = "select * from (select p.seq " +
	" from t_product p " +
	" where p.seq <> ? and purchasedboth(?, p.seq) <> 0" +
	" order by purchasedboth(?, p.seq) desc) " +
	" where rownum <= 2";

	public void addRelatedProducts(Transaction transaction, RelatedProductDto rpd) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);

			for (ProductDto dto: rpd.getRelated()) {
				prepareStatement = connection.prepareStatement(ADD_RELATED_PRODUCTS);
				prepareStatement.setInt(1, rpd.getProduct().getSeq());
				prepareStatement.setInt(2, dto.getSeq());
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
}
