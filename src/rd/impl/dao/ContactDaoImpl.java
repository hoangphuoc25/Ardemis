package rd.impl.dao;

import java.io.IOException;
import java.io.Serializable;
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
import rd.spec.dao.CompanyDao;
import rd.spec.dao.ContactDao;
import rd.spec.dao.Transaction;

public class ContactDaoImpl implements ContactDao, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private CompanyDao comDao;

	@Inject
	public ContactDaoImpl(CompanyDao comDao) {
		this.comDao = comDao;
	}

	public void addContact (Transaction transaction, ContactDto contact) throws IOException {
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
			prepareStatement.setInt(6, contact.getCompany().getSeq());
			prepareStatement.setString(7, contact.getLanguage());
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

			int result = -1;
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
	public ContactDto getContactById (Transaction transaction, int seq) throws IOException {
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

	public void updateContact (Transaction transaction, ContactDto contact) throws IOException {
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
			prepareStatement.setInt(5, contact.getCompany().getSeq());
			prepareStatement.setString(6, contact.getLanguage());
			prepareStatement.setInt(7, contact.getSeq());

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
	public void deleteContact (Transaction transaction, int seq) throws IOException {
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

	private static final String GET_SEQ = "select max(seq) from t_contact";
	private static final String DELETE_CONTACT = "delete from t_contact where seq=?";
	private static final String UPDATE_CONTACT = "update t_contact set name=?, gender=?, phone=?, email=?, company_seq=?, language=? where seq=?";
	private static final String GET_CONTACT_BY_ID = "select seq, name, gender, phone, email, company_seq, language from t_contact where seq=?";
	private static final String ADD_CONTACT = "insert into t_contact (seq, name, gender, phone, email, company_seq, language) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_BY_COMPANY = "select seq, name, gender, phone, email, company_seq, language from t_contact where company_seq=?";

	public List<ContactDto> getByCompany(Transaction transaction, CompanyDto company) throws IOException {
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

	private ContactDto makeContactDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		int seq = resultSet.getInt(1);
		String name = resultSet.getString(2);
		String gender = resultSet.getString(3);
		String phone = resultSet.getString(4);
		String email = resultSet.getString(5);
		CompanyDto company = comDao.getById(transaction, resultSet.getInt(6));
		String language = resultSet.getString(7);

		return new ContactDto(seq, name, gender, phone, email, company, language);
	}

}
