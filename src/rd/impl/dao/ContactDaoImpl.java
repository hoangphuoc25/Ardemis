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

import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.dao.ContactDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class ContactDaoImpl implements ContactDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String GET_SEQ 			= "select max(seq)+1 from t_contact";
	private static final String DELETE_CONTACT 		= "delete from t_contact where seq=?";
	private static final String UPDATE_CONTACT 		= "update t_contact set name=?, gender=?, phone=?, email=?, company=?, language=?, address=?, salesperson=?, contact_status=? where seq=?";
	private static final String GET_CONTACT_BY_ID 	= "select seq, name, gender, phone, email, company, language, address, salesperson, contact_status from t_contact where seq=?";
	private static final String ADD_CONTACT 		= "insert into t_contact (seq, name, gender, phone, email, company, language, address, salesperson, contact_status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_BY_COMPANY 		= "select seq, name, gender, phone, email, company, language, address, salesperson, contact_status from t_contact where company=?";
	private static final String SEARCH_CONTACT_BY_NAME 	= "select seq, name, gender, phone, email, company, language, address, salesperson, contact_status from t_contact where lower(name) like ?";
	private static final String GET_ALL 			= "select seq, name, gender, phone, email, company, language, address, salesperson, contact_status from t_contact";
	private static String GET_BY_STATUS_AND_USER 	= "select seq, name, gender, phone, email, company, language, address, salesperson, contact_status from t_contact where lower(contact_status)=? and salesperson=?";
	private static String GET_BY_STATUS 			= "select seq, name, gender, phone, email, company, language, address, salesperson, contact_status from t_contact where lower(contact_status)=?";
	private static String GET_NUMBER_OF_CONTACT_PER_SALE = "select count(*) from t_contact where lower(contact_status)='new' and salesperson=?";
	private static String ADD_COMPANY_CONTACT 		= "insert into t_company_contact (company_seq, contact_seq) values (?, ?)";

	private UserDao userDao;

	@Inject
	public ContactDaoImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	public void addContact(Transaction transaction, ContactDto contact)
			throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_CONTACT);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, contact.getName());
			prepareStatement.setString(3, contact.getGender());
			prepareStatement.setString(4, contact.getPhone());
			prepareStatement.setString(5, contact.getEmail());
			prepareStatement.setString(6, contact.getCompany());
			prepareStatement.setString(7, contact.getLanguage());
			prepareStatement.setString(8, contact.getAddress());
			prepareStatement.setString(9, contact.getAssignee().getId());
			prepareStatement.setString(10, contact.getContactStatus());

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

			int result = 0;
			if (resultSet.next()) {
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

	public ContactDto getContactById(Transaction transaction, int seq)
			throws IOException {
		// TODO
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_CONTACT_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			ContactDto result = new ContactDto();
			while (resultSet.next()) {
				result = makeContactDto(transaction, resultSet);
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

	public void updateContact(Transaction transaction, ContactDto contact)
			throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_CONTACT);
			prepareStatement.setString(1, contact.getName());
			prepareStatement.setString(2, contact.getGender());
			prepareStatement.setString(3, contact.getPhone());
			prepareStatement.setString(4, contact.getEmail());
			prepareStatement.setString(5, contact.getCompany());
			prepareStatement.setString(6, contact.getLanguage());
			prepareStatement.setString(7, contact.getAddress());
			prepareStatement.setString(8, contact.getAssignee().getId());
			prepareStatement.setString(9, contact.getContactStatus());
			prepareStatement.setInt(10, contact.getSeq());

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

	public void deleteContact(Transaction transaction, int seq)
			throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_CONTACT);
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

	public List<ContactDto> getByCompany(Transaction transaction,
			CompanyDto company) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_COMPANY);
			prepareStatement.setInt(1, company.getSeq());
			resultSet = prepareStatement.executeQuery();

			List<ContactDto> contacts = new ArrayList<ContactDto>();
			while (resultSet.next()) {
				contacts.add(makeContactDto(transaction, resultSet));
			}
			return contacts;
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

	private ContactDto makeContactDto(Transaction transaction,
			ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		String name = resultSet.getString(2);
		String gender = resultSet.getString(3);
		String phone = resultSet.getString(4);
		String email = resultSet.getString(5);
		String company = resultSet.getString(6);
		String language = resultSet.getString(7);
		String address = resultSet.getString(8);
		UserDto sale = userDao.findUser(transaction, resultSet.getString(9));
		String contactStatus = resultSet.getString(10);

		return new ContactDto(seq, name, gender, phone, email, company, language, address, sale, contactStatus);
	}
	public List<ContactDto> searchContactByName(Transaction transaction, String partial) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_CONTACT_BY_NAME);
			prepareStatement.setString(1, "%" + partial.toLowerCase() + "%");
			resultSet = prepareStatement.executeQuery();

			List<ContactDto> result = new ArrayList<ContactDto>();
			while (resultSet.next()) {
				result.add(makeContactDto(transaction, resultSet));
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
	public List<ContactDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<ContactDto> result = new ArrayList<ContactDto>();
			while (resultSet.next()) {
				result.add(makeContactDto(transaction, resultSet));
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

	public List<ContactDto> getByStatusAndUser(Transaction transaction, String status, String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_STATUS_AND_USER);
			prepareStatement.setString(1, status.toLowerCase());
			prepareStatement.setString(2, userId);
			resultSet = prepareStatement.executeQuery();

			List<ContactDto> result = new ArrayList<ContactDto>();
			while (resultSet.next()) {
				result.add(makeContactDto(transaction, resultSet));
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
	public List<ContactDto> getByStatus(Transaction transaction, String status) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_STATUS);
			prepareStatement.setString(1, status.toLowerCase());
			resultSet = prepareStatement.executeQuery();

			List<ContactDto> result = new ArrayList<ContactDto>();
			while (resultSet.next()) {
				result.add(makeContactDto(transaction, resultSet));
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
	public int getNumberOfContactPerSale(Transaction transaction, String saleId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_NUMBER_OF_CONTACT_PER_SALE);
			prepareStatement.setString(1, saleId);
			resultSet = prepareStatement.executeQuery();

			int result = 0;
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

	public void addCompanyContact(Transaction transaction, ContactDto contact,CompanyDto company) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_COMPANY_CONTACT);
			prepareStatement.setInt(1, company.getSeq());
			prepareStatement.setInt(2, contact.getSeq());
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
	public void registerInterest(Transaction transaction, ContactDto contact,List<ProductDto> products) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			for (ProductDto prod: products) {
				prepareStatement = connection.prepareStatement(REGISTER_INTEREST);
				prepareStatement.setInt(1, contact.getSeq());
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
	private static String REGISTER_INTEREST = "insert into t_contact_product (contact_seq, product_seq) values (?, ?)";
}
