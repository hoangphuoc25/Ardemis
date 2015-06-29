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
import rd.dto.SupportTicketDto;
import rd.spec.dao.CompanyDao;
import rd.spec.dao.SupportTicketDao;
import rd.spec.dao.Transaction;

public class SupportTicketDaoImpl implements SupportTicketDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private CompanyDao companyDao;

	public List<SupportTicketDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<SupportTicketDto> result = new ArrayList<SupportTicketDto>();
			while (resultSet.next()) {
				result.add(makeSupportTicketDto(transaction, resultSet));
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

	private static String GET_ALL = "select seq, name, company_seq, phone, email, category_seq, detail, status from t_support_ticket";

	public List<SupportTicketDto> getPendingTicket(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_PENDING_TICKET);

			resultSet = prepareStatement.executeQuery();
			List<SupportTicketDto> result = new ArrayList<SupportTicketDto>();
			while (resultSet.next()) {
				result.add(makeSupportTicketDto(transaction, resultSet));
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

	private static String GET_PENDING_TICKET = "select seq, name, company_seq, phone, email, category_seq, detail, status from t_support_ticket where status<>'RESOLVED'";

	public List<SupportTicketDto> getTicketByName(Transaction transaction, String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_TICKET_BY_NAME);
			prepareStatement.setString(1, "%" + name + "%");
			resultSet = prepareStatement.executeQuery();

			List<SupportTicketDto> result = new ArrayList<SupportTicketDto>();
			while (resultSet.next()) {
				result.add(makeSupportTicketDto(transaction, resultSet));
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

	private static String GET_TICKET_BY_NAME = "select seq, name, company_seq, phone, email, category_seq, detail, status from t_support_ticket where name like ?";

	public List<SupportTicketDto> getTicketByCompany(Transaction transaction, String company) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_TICKET_BY_COMPANY);
			resultSet = prepareStatement.executeQuery();

			List<SupportTicketDto> result = new ArrayList<SupportTicketDto>();
			while (resultSet.next()) {

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

	private SupportTicketDto makeSupportTicketDto(Transaction transaction, ResultSet result) throws IOException, SQLException {
		int seq = result.getInt(1);
		String name = result.getString(2);
		CompanyDto company = companyDao.getById(transaction, result.getInt(3));
		String phone = result.getString(4);
		String email = result.getString(5);
		int category = result.getInt(6);
		String detail = result.getString(7);
		String status = result.getString(8);
		return new SupportTicketDto(seq, name, company, category, phone, email, detail, status, "");
	}

	private static String GET_TICKET_BY_COMPANY = "select seq, name, company_seq, phone, email, category_seq, detail, status from t_support_ticket";
}
