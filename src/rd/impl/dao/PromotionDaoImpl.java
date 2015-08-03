package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ProductDto;
import rd.dto.PromotionDto;
import rd.spec.dao.ProductDao;
import rd.spec.dao.PromotionDao;
import rd.spec.dao.Transaction;

public class PromotionDaoImpl implements PromotionDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ProductDao prodDao;

	@Inject
	public PromotionDaoImpl(ProductDao prodDao) {
		this.prodDao = prodDao;
	}

	private static String GET_SEQ = "select max(seq)+1 from t_promo";
	private static String GET_ALL = "select seq, start_date, end_date, discount from t_promo";
	private static String ADD_PROMOTION = "insert into t_promo (seq, start_date, end_date, discount) values (?, ?, ?, ?)";
	private static String ADD_PROMO_PRODUCT = "insert into t_promo_product (promo_seq, product_seq) values (?, ?)";
	private static String GET_BY_ID = "select seq, start_date, end_date, discount from t_promo where seq=?";
	private static String GET_BY_PRODUCT = "select distinct p.seq from t_promo p join t_promo_product pp on p.seq=pp.promo_seq where pp.product_seq=?";
	private static String DELETE_PROMOTION = "delete from t_promo where seq=?";
	private static String DELETE_PROMO_PRODUCT = "delete from t_promo_product where promo_seq=?";
	private static String GET_ACTIVE = "select seq, start_date, end_date, discount from t_promo where start_date <= ? and end_date >= ?";
	private static String GET_PRODUCT_LIST = "select product_seq from t_promo_product where promo_seq=?";

	public void addPromotion(Transaction transaction, PromotionDto promo) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_PROMOTION);
			promo.setSeq(getSeq(transaction));
			prepareStatement.setInt(1, promo.getSeq());
			prepareStatement.setDate(2, new java.sql.Date(promo.getStartDate().getTime()));
			prepareStatement.setDate(3, new java.sql.Date(promo.getEndDate().getTime()));
			prepareStatement.setInt(4, promo.getDiscount());
			resultSet = prepareStatement.executeQuery();

			for (ProductDto prod: promo.getProductList()) {
				prepareStatement = connection.prepareStatement(ADD_PROMO_PRODUCT);
				prepareStatement.setInt(1, promo.getSeq());
				prepareStatement.setInt(2, prod.getSeq());
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

	public PromotionDto getById(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			PromotionDto result = null;
			while (resultSet.next()) {
				result = new PromotionDto();
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

	public List<PromotionDto> getByProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<PromotionDto> result = new ArrayList<PromotionDto>();
			while (resultSet.next()) {
				result.add(getById(transaction, resultSet.getInt(1)));
			}
			System.out.println(result.size());
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

	public void deletePromotion(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_PROMOTION);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			prepareStatement = connection.prepareStatement(DELETE_PROMO_PRODUCT);
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

	public void updatePromotion(Transaction transaction, PromotionDto promo) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			deletePromotion(transaction, promo.getSeq());
			addPromotion(transaction, promo);
		} catch (Exception e) {
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

	public List<PromotionDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<PromotionDto> result = new ArrayList<PromotionDto>();
			while (resultSet.next()) {
				result.add(makePromoDto(transaction, resultSet));
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

	public List<PromotionDto> getActive(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {

			Calendar date = new GregorianCalendar();
			date.setTime(new Date());
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ACTIVE);
			prepareStatement.setDate(1, new java.sql.Date(date.getTime().getTime()));
			prepareStatement.setDate(2, new java.sql.Date(date.getTime().getTime()));
			resultSet = prepareStatement.executeQuery();

			List<PromotionDto> result = new ArrayList<PromotionDto>();
			while (resultSet.next()) {
				result.add(makePromoDto(transaction, resultSet));
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

	private PromotionDto makePromoDto(Transaction transaction, ResultSet resultSet) throws SQLException {
		int seq = resultSet.getInt(1);
		Date startDate = new Date(resultSet.getDate(2).getTime());
		Date endDate = new Date(resultSet.getDate(3).getTime());
		int discount = resultSet.getInt(4);
		return new PromotionDto(seq, startDate, endDate, null, discount);
	}
	public List<ProductDto> getProductList(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PRODUCT_LIST);
			prepareStatement.setInt(1, seq);
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
}
