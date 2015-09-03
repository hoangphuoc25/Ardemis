package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.ActivityDto;
import rd.dto.ContactDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.dao.ActivityDao;
import rd.spec.dao.ContactDao;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;
import rd.spec.dataCache.ActivityCache;

public class ActivityDaoImpl implements ActivityDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ContactDao contactDao;
	private UserDao userDao;
	private ActivityCache actCache;
	private ProductDao prodDao;

	@Inject
	public ActivityDaoImpl(ContactDao contactDao, UserDao userDao, ActivityCache actCache, ProductDao prodDao) {
		this.contactDao = contactDao;
		this.userDao = userDao;
		this.actCache = actCache;
		this.prodDao = prodDao;
	}

	private static String ADD_ACTIVITY 		= "insert into t_activity (seq, contact_seq, start_date, status, remark, salesperson) values (?, ?, ?, ?, ?, ?)";
	private static String GET_BY_ID 		= "select seq, contact_seq, start_date, status, remark, salesperson from t_activity where seq=?";
	private static String GET_SEQ 			= "select max(seq) + 1 from t_activity";
	private static String GET_BY_USER 		= "select seq, contact_seq, start_date, status, remark, salesperson from t_activity where salesperson=? order by seq desc";
	private static String DELETE_ACTIVITY 	= "delete from t_activity where seq=?";
	private static String UPDATE_ACTIVITY 	= "update t_activity set contact_seq=?, start_date=?, status=?, remark=?, salesperson=? where seq=?";
	private static String FIND_BY_STATUS 	= "select seq, contact_seq, start_date, status, remark, salesperson from t_activity where salesperson=? and lower(status)=? order by seq desc";
	private static String GET_ACTIVE_DEAL 	= "select seq, contact_seq, start_date, status, remark, salesperson from t_activity where lower(status) <> ? and lower(status) <> ? order by seq desc";
	private static String ADD_DEAL_PRODUCT	= "insert into t_deal_product (deal_seq, product_seq) values (?, ?)";
	private static String DELETE_DEAL_PRODUCT= "delete from t_deal_product where deal_seq=?";
	private static String GET_PRODUCT_BY_DEAL = "select distinct product_seq from t_deal_product where deal_seq=?";

	public void addActivity(Transaction transaction, ActivityDto act)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_ACTIVITY);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setInt(2, act.getContact().getSeq());
			prepareStatement.setDate(3, new java.sql.Date(act.getStartDate().getTime()));
			prepareStatement.setString(4, act.getStatus());
			prepareStatement.setString(5, act.getRemark());
			prepareStatement.setString(6, act.getSalesperson().getId());
			resultSet = prepareStatement.executeQuery();

			actCache.put(act);

			if (act.getProducts() != null) {
				for (ProductDto dto: act.getProducts()) {
					prepareStatement = connection.prepareStatement(ADD_DEAL_PRODUCT);
					prepareStatement.setInt(1, getSeq(transaction));
					prepareStatement.setInt(2, dto.getSeq());
					resultSet = prepareStatement.executeQuery();
				}
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

	public ActivityDto getById(Transaction transaction, int seq)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		ActivityDto act = actCache.get(seq);
		if (act != null) {
			return act;
		}

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			ActivityDto result = null;
			if (resultSet.next()) {
				result = makeActivityDto(transaction, resultSet);

				actCache.put(result);
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

	public List<ActivityDto> getByUser(Transaction transaction, String username)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_USER);
			prepareStatement.setString(1, username);
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = makeActivityDto(transaction, resultSet);
				result.add(act);
				actCache.put(act);
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

	public void updateActivity(Transaction transaction, ActivityDto act)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_ACTIVITY);
			prepareStatement.setInt(1, act.getContact().getSeq());
			prepareStatement.setDate(2, new java.sql.Date(act.getStartDate().getTime()));
			prepareStatement.setString(3, act.getStatus());
			prepareStatement.setString(4, act.getRemark());
			prepareStatement.setString(5, act.getSalesperson().getId());
			prepareStatement.setInt(6, act.getSeq());

			resultSet = prepareStatement.executeQuery();

			actCache.put(act);

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

	public void deleteActivity(Transaction transaction, ActivityDto act)
			throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_ACTIVITY);
			prepareStatement.setInt(1, act.getSeq());
			resultSet = prepareStatement.executeQuery();

			prepareStatement = connection.prepareStatement(DELETE_DEAL_PRODUCT);
			prepareStatement.setInt(1, act.getSeq());
			resultSet = prepareStatement.executeQuery();

			actCache.remove(act.getSeq());

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

	private ActivityDto makeActivityDto(Transaction transaction,
			ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		ContactDto contact = contactDao.getContactById(transaction, resultSet.getInt(2));
		Date startDate = new Date(resultSet.getDate(3).getTime());
		String status = resultSet.getString(4);
		String remark = resultSet.getString(5);
		UserDto salesperson = userDao.findUser(transaction, resultSet.getString(6));
		// List<ProductDto> prods = getProductByDeal(transaction, resultSet.getInt(1));
		List<ProductDto> prods = prodDao.getProductByDeal(transaction, seq);

		return new ActivityDto(seq, contact, startDate, status, remark, salesperson, prods);
	}

	private ActivityDto makeActivityDto(Transaction transaction, ResultSet resultSet, Map<Integer, ProductDto> prods, Map<Integer, List<Integer>> dealProd) throws IOException, SQLException {
		int seq = resultSet.getInt(1);
		ContactDto contact = contactDao.getContactById(transaction, resultSet.getInt(2));
		Date startDate = new Date(resultSet.getDate(3).getTime());
		String status = resultSet.getString(4);
		String remark = resultSet.getString(5);
		UserDto salesperson = userDao.findUser(transaction, resultSet.getString(6));

		List<ProductDto> temp = new ArrayList<ProductDto>();
		if (dealProd.containsKey(seq)) {
			for (int i: dealProd.get(seq)) {
				temp.add(prods.get(i));
			}
		}
		return new ActivityDto(seq, contact, startDate, status, remark, salesperson, temp);
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
	public List<ActivityDto> findByStatus(Transaction transaction, String status, String username) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(FIND_BY_STATUS);
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, status.toLowerCase());
			resultSet = prepareStatement.executeQuery();

			System.out.println("ActivityDaoImpl.findByStatus()");
			Map<Integer, ProductDto> prods = prodDao.getProductByUserAndStatus(transaction, username, status);
			System.out.println(prods.size());
			for (Entry<Integer, ProductDto> entry : prods.entrySet()) {
			    System.out.println(entry.getValue().getName());
			}

			Map<Integer, List<Integer>> dealProds = getDealProducts(transaction, username, status);
			System.out.println(username);
			System.out.println(status);
			System.out.println("DEALPRODS : " +dealProds.size());


			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = makeActivityDto(transaction, resultSet, prods, dealProds);
				result.add(act);
				actCache.put(act);
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
	public List<ActivityDto> getActiveDeal(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ACTIVE_DEAL);
			prepareStatement.setString(1, "completed");
			prepareStatement.setString(2, "failed");
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = makeActivityDto(transaction, resultSet);
				result.add(act);
				actCache.put(act);
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
	public List<ProductDto> getProductByDeal(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PRODUCT_BY_DEAL);
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

	public List<ActivityDto> searchByCustomerName(Transaction transaction, String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_BY_CUSTOMER_NAME);
			prepareStatement.setString(1, "%" + name.toLowerCase() + "%");
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = getById(transaction, resultSet.getInt(1));
				result.add(act);
				actCache.put(act);
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
	private static String SEARCH_BY_CUSTOMER_NAME = "select distinct a.seq from t_activity a join t_contact c on a.contact_seq = c.seq where lower(c.name) like ?";
	public List<ActivityDto> getActiveDealByContact(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ACTIVE_DEAL_BY_CONTACT);
			prepareStatement.setString(1, "completed");
			prepareStatement.setString(2, "failed");
			prepareStatement.setInt(3, seq);
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = makeActivityDto(transaction, resultSet);
				result.add(act);
				actCache.put(act);
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
	private static String GET_ACTIVE_DEAL_BY_CONTACT = "select seq, contact_seq, start_date, status, remark, salesperson from t_activity where lower(status) <> ? and lower(status) <> ? and contact_seq=?";
	public List<ActivityDto> getActiveDealByCompany(Transaction transaction, String company) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ACTIVE_DEAL_BY_COMPANY);
			prepareStatement.setString(1, "completed");
			prepareStatement.setString(2, "failed");
			prepareStatement.setString(3, "%" + company.toLowerCase() + "%");
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = makeActivityDto(transaction, resultSet);
				result.add(act);
				actCache.put(act);
			}
			return result;		} catch (SQLException e) {
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
	private static String GET_ACTIVE_DEAL_BY_COMPANY = "select a.seq, a.contact_seq, a.start_date, a.status, a.remark, a.salesperson from t_activity a join t_contact c on a.contact_seq=c.seq where lower(a.status) <> ? and lower(a.status) <> ? and lower(c.company) like ?";
	public List<ActivityDto> getActiveDealBySalesperson(Transaction transaction, String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ACTIVE_DEAL_BY_SALESPERSON);
			prepareStatement.setString(1, "completed");
			prepareStatement.setString(2, "failed");
			prepareStatement.setString(3, userId);
			resultSet = prepareStatement.executeQuery();

			List<ActivityDto> result = new ArrayList<ActivityDto>();
			while (resultSet.next()) {
				ActivityDto act = makeActivityDto(transaction, resultSet);
				result.add(act);
				actCache.put(act);
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
	private static String GET_ACTIVE_DEAL_BY_SALESPERSON = "select seq, contact_seq, start_date, status, remark, salesperson from t_activity where lower(status) <> ? and lower(status) <> ? and salesperson=?";

	private Map<Integer, List<Integer>> getDealProducts(Transaction transaction, String userId, String status) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_DEAL_PRODUCTS_BY_USER);
			prepareStatement.setString(1, userId);
			prepareStatement.setString(2, status.toLowerCase());
			resultSet = prepareStatement.executeQuery();

			return makeDealProductMap(resultSet);

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

	private Map<Integer, List<Integer>> makeDealProductMap(ResultSet resultSet) throws SQLException {
		Map<Integer, List<Integer>> result = new Hashtable<Integer, List<Integer>>();
		while (resultSet.next()) {
			int dealSeq = resultSet.getInt(1);
			int prodSeq = resultSet.getInt(2);
			if (result.containsKey(dealSeq)) {
				result.get(dealSeq).add(prodSeq);
			} else {
				result.put(dealSeq, new ArrayList<Integer>(Arrays.asList(prodSeq)));
			}
		}
		return result;
	}

	private final String GET_DEAL_PRODUCTS_BY_USER = "select dp.deal_seq, dp.product_seq from t_deal_product dp where dp.deal_seq in (select seq from t_activity where salesperson=? and lower(status)=?)";
}
