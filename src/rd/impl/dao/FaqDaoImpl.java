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

import rd.dto.FaqDto;
import rd.dto.ProductDto;
import rd.spec.dao.FaqDao;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;

public class FaqDaoImpl implements FaqDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ProductDao prodDao;

	@Inject
	public FaqDaoImpl(ProductDao prodDao) {
		this.prodDao = prodDao;
	}

	private static String GET_SEQ 			= "select max(seq) + 1 from t_faq";
	private static String GET_BY_PRODUCT 	= "select seq, product_seq, question, answer from t_faq where product_seq=?";
	private static String ADD_FAQ 			= "insert into t_faq (seq, product_seq, question, answer) values (?, ?, ?, ?)";
	private static String UPDATE_FAQ 		= "update t_faq set product_seq=?, question=?, answer=? where seq=?";
	private static String DELETE_FAQ 		= "delete from t_faq where seq=?";
	private static String GET_ALL 			= "select seq, product_seq, question, answer from t_faq";
	private static String GET_BY_ID			= "select seq, product_seq, question, answer from t_faq where seq=?";
	public void addFaq(Transaction transaction, FaqDto faq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_FAQ);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, faq.getProduct().getSeq());
			prepareStatement.setString(3, faq.getQuestion());
			prepareStatement.setString(4, faq.getAnswer());

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



	public void updateFaq(Transaction transaction, FaqDto faq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_FAQ);
			prepareStatement.setInt(1, faq.getProduct().getSeq());
			prepareStatement.setString(2, faq.getQuestion());
			prepareStatement.setString(3, faq.getAnswer());

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



	public void deleteFaq(Transaction transaction, FaqDto faq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_FAQ);
			prepareStatement.setInt(1, faq.getSeq());
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



	public List<FaqDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<FaqDto> result = new ArrayList<FaqDto>();
			while (resultSet.next()) {
				result.add(makeFaqDto(transaction, resultSet));
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



	public List<FaqDto> getByProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<FaqDto> result = new ArrayList<FaqDto>();
			while (resultSet.next()) {
				result.add(makeFaqDto(transaction, resultSet));
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
			if (resultSet.next()) {
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
	public FaqDto getById(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			FaqDto faq = new FaqDto();
			if (resultSet.next()) {
				faq = makeFaqDto(transaction, resultSet);
			}
			return faq;

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

	private FaqDto makeFaqDto(Transaction transaction, ResultSet resultSet) throws IOException, SQLException {
		int seq = resultSet.getInt(1);
		ProductDto product = prodDao.getProductById(transaction, resultSet.getInt(2));
		String question = resultSet.getString(3);
		String answer = resultSet.getString(4);
		return new FaqDto(seq, product, question, answer);
	}
}
