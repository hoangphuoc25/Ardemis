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

	@Inject
	ProductDao prodDao;
	@Inject
	UserDao userDao;

	private static String GET_SEQ = "select max(seq)+1 from t_feedback";
	private static String ADD_FEEDBACK = "insert into t_feedback (seq, product_id, rating, user_id, feedback, feature, "
			+ "third_party_support, performance, user_ex, user_in, usability, stability) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static String GET_BY_ID = "select seq, product_id, rating, user_id, feedback, feature, third_party_support, performance, user_ex, user_in, usability, stability from t_feedback where seq=?";
	private static String GET_FEEDBACK_BY_PRODUCT = "select seq, product_id, rating, user_id, feedback, feature, third_party_support, performance, user_ex, user_in, usability, stability from t_feedback where product_id=?";

	private static String GET_AVERAGE_FEATURE = "select avg(feature) from t_feedback where feature>0 and product_id=?";
	private static String GET_AVERAGE_THIRD_PARTY_SUPPORT = "select avg(third_party_support) from t_feedback where third_party_support>0 and product_id=?";
	private static String GET_AVERAGE_PERFORMANCE = "select avg(performance) from t_feedback where performance>0 and product_id=?";
	private static String GET_AVERAGE_USER_EX = "select avg(user_ex) from t_feedback where user_ex>0 and product_id=?";
	private static String GET_AVERAGE_USER_IN = "select avg(user_in) from t_feedback where user_in>0 and product_id=?";
	private static String GET_AVERAGE_USABILITY = "select avg(usability) from t_feedback where usability>0 and product_id=?";
	private static String GET_AVERAGE_STABILITY = "select avg(stability) from t_feedback where stability>0 and product_id=?";

	public List<FeedbackDto> getFeedbackByProduct(Transaction transaction,
			int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection
					.prepareStatement(GET_FEEDBACK_BY_PRODUCT);
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

	public FeedbackDto getById(Transaction transaction, int seq)
			throws IOException {
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

	public void addFeedback(Transaction transaction, FeedbackDto fb)
			throws IOException {
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

			prepareStatement.setInt(6, fb.getFeature());
			prepareStatement.setInt(7, fb.getThirdPartySupport());
			prepareStatement.setInt(8, fb.getPerformance());
			prepareStatement.setInt(9, fb.getUserExperience());
			prepareStatement.setInt(10, fb.getUserInterface());
			prepareStatement.setInt(11, fb.getUsability());
			prepareStatement.setInt(12, fb.getStability());

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

	private FeedbackDto makeFeedbackDto(Transaction transaction,
			ResultSet resultSet) throws IOException, SQLException {
		int seq = resultSet.getInt(1);
		ProductDto product = prodDao.getProductById(transaction,
				resultSet.getInt(2));
		int rating = resultSet.getInt(3);
		UserDto user = userDao.findUser(transaction, resultSet.getString(4));
		String feedback = resultSet.getString(5);
		int feature = resultSet.getInt(6);
		int thirdPartySupport = resultSet.getInt(7);
		int performance = resultSet.getInt(8);
		int userExperience = resultSet.getInt(9);
		int userInterface = resultSet.getInt(10);
		int usability = resultSet.getInt(11);
		int stability = resultSet.getInt(12);
		return new FeedbackDto(seq, product, rating, user, feedback, feature,
				thirdPartySupport, performance, userExperience, userInterface,
				usability, stability);
	}
	public double getAverageFeature(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_FEATURE);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public double getAverageThirdPartySupport(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_THIRD_PARTY_SUPPORT);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public double getAveragePerformance(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_PERFORMANCE);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public double getAverageUserEx(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_USER_EX);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public double getAverageUserIn(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_USER_IN);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public double getAverageUsability(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_USABILITY);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public double getAverageStability(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_AVERAGE_STABILITY);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			double result = 0;
			if (resultSet.next()) {
				result = resultSet.getDouble(1);
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

	public String getHappiness(Transaction transaction, int contactSeq, int prodSeq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_HAPPINESS);
			prepareStatement.setInt(1, contactSeq);
			prepareStatement.setInt(2, prodSeq);
			resultSet = prepareStatement.executeQuery();

			String result = null;
			if (resultSet.next()) {
				result = resultSet.getString(1);
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
	private static String GET_HAPPINESS = "select happiness from t_client_happiness where contact_seq=? and product_seq=?";
	public void addClientHappiness(Transaction transaction, int contactSeq, int productSeq, String happiness) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_CLIENT_HAPPINESS);
			prepareStatement.setInt(1, contactSeq);
			prepareStatement.setInt(2, productSeq);
			prepareStatement.setString(3, happiness);

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
	private static String ADD_CLIENT_HAPPINESS = "insert into t_client_happiness (contact_seq, product_seq, happiness) values (?, ?, ?)";
}
