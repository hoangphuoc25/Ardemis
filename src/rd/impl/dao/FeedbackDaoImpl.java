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

import rd.dto.FeedbackDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.dao.FeedbackDao;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class FeedbackDaoImpl implements FeedbackDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject ProductDao prodDao;
	@Inject UserDao userDao;

	public List<FeedbackDto> getFeedbackByProduct(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_FEEDBACK_BY_PRODUCT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<FeedbackDto> result = new ArrayList<FeedbackDto>();
			while (resultSet.next()) {
				result.add(makeFeedbackDto(transaction, resultSet));
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

	private static String GET_FEEDBACK_BY_PRODUCT = "select seq, product_id, rating, user_id, feedback from t_feedback where product_id=?";

	public FeedbackDto getById(Transaction transaction, int seq) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			FeedbackDto result = null;
			while (resultSet.next()) {
				result = makeFeedbackDto(transaction, resultSet);
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

	private static String GET_BY_ID = "select seq, product_id, rating, user_id, feedback from t_feedback where seq=?";

	public void addFeedback(Transaction transaction, FeedbackDto fb) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_FEEDBACK);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, fb.getProduct().getSeq());
			prepareStatement.setInt(3, fb.getRating());
			prepareStatement.setString(4, fb.getCustomer().getId());
			prepareStatement.setString(5, fb.getFeedback());
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

	private static String ADD_FEEDBACK = "insert into t_feedback (seq, product_id, rating, user_id, feedback) values (?, ?, ?, ?, ?)";

	public int getSeq(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int result = 1;
			while (resultSet.next()) {
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

	private static String GET_SEQ = "select max(seq)+1 from t_feedback";

	private FeedbackDto makeFeedbackDto(Transaction transaction, ResultSet resultSet) throws IOException, SQLException {
		int seq = resultSet.getInt(1);
		ProductDto product = prodDao.getProductById(transaction, resultSet.getInt(2));
		int rating = resultSet.getInt(3);
		UserDto user = userDao.findUser(transaction, resultSet.getString(4));
		String feedback = resultSet.getString(5);
		return new FeedbackDto(seq, product, rating, user, feedback);
	}
}
