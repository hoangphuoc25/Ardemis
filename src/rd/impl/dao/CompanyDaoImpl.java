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
import rd.spec.dao.CompanyDao;
import rd.spec.dao.Transaction;
import rd.spec.dataCache.CompanyCache;

public class CompanyDaoImpl implements CompanyDao, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private CompanyCache comCache;

	@Inject
	public CompanyDaoImpl(CompanyCache comCache) {
		this.comCache = comCache;
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String GET_SEQ = "select max(seq)+1 from t_clientcompany";
	private static final String GET_COMPANY_BY_INDUSTRY = "select seq, name, com_size, industry, com_type, year_founded, location, phone, remark from t_clientcompany where industry=?";
	private static final String UPDATE_COMPANY = "update t_clientcompany set name=?, com_size=?, industry=?, com_type=?, year_founded=?, location=?, phone=?, remark=? where seq=?";
	private static final String DELETE_COMPANY = "delete from t_clientcompany where seq=?";
	private static final String INSERT_COMPANY= "insert into t_clientcompany (seq, name, com_size, industry, com_type, year_founded, location, phone, remark) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static String GET_BY_ID = "select seq, name, com_size, industry, com_type, year_founded, location, phone, remark from t_clientcompany where seq=?";
	private static String GET_BY_INDUSTRY = "select seq, name, com_size, industry, com_type, year_founded, location, phone, remark from t_clientcompany where industry like ?";

	public int getSeq(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int result = -1;
			if(resultSet.next()) {
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

	public void updateCompany(Transaction transaction, CompanyDto com) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_COMPANY);
			prepareStatement.setString(1, com.getName());
			prepareStatement.setString(2, com.getSize());
			prepareStatement.setString(3, com.getIndustry());
			prepareStatement.setString(4, com.getType());
			prepareStatement.setInt(5, com.getYearFounded());
			prepareStatement.setString(6, com.getLocation());
			prepareStatement.setString(7, com.getPhone());
			prepareStatement.setString(8, com.getRemark());
			resultSet = prepareStatement.executeQuery();

			comCache.put(com);

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

	public void deleteCompany(Transaction transaction, int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_COMPANY);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			comCache.remove(seq);

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

	public void insertCompany(Transaction transaction, CompanyDto comp) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(INSERT_COMPANY);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, comp.getName());
			prepareStatement.setString(3, comp.getSize());
			prepareStatement.setString(4, comp.getIndustry());
			prepareStatement.setString(5, comp.getType());
			prepareStatement.setInt(6, comp.getYearFounded());
			prepareStatement.setString(7, comp.getLocation());
			prepareStatement.setString(8, comp.getPhone());
			prepareStatement.setString(9, comp.getRemark());

			resultSet = prepareStatement.executeQuery();

			comCache.put(comp);

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

	public CompanyDto getById(Transaction transaction, int seq) throws IOException {
		if (comCache.getCompany(seq) != null) {
			return comCache.getCompany(seq);
		}

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();
			CompanyDto result = new CompanyDto();
			while (resultSet.next()) {
				result = makeCompanyDto(resultSet);
				comCache.put(makeCompanyDto(resultSet));
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

	private boolean isDirty(CompanyDto company) {
		return false;
	}

	public List<CompanyDto> getByIndustry(Transaction transaction, String industry) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_INDUSTRY);
			prepareStatement.setString(1, "%" + industry + "%");
			resultSet = prepareStatement.executeQuery();

			List<CompanyDto> result = new ArrayList<CompanyDto>();
			while (resultSet.next()) {
				result.add(makeCompanyDto(resultSet));
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

	public List<CompanyDto> searchCompanyByName(Transaction transaction, String name) throws IOException {
		// TODO
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_COMPANY_BY_NAME);
			prepareStatement.setString(1, "%" + name + "%");
			resultSet = prepareStatement.executeQuery();

			List<CompanyDto> result = new ArrayList<CompanyDto>();
			while (resultSet.next()) {
				result.add(makeCompanyDto(resultSet));
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
	private CompanyDto makeCompanyDto(ResultSet resultSet) throws SQLException {
		int seq = resultSet.getInt(1);
		String name = resultSet.getString(2);
		String size = resultSet.getString(3);
		String industry = resultSet.getString(4);
		String type = resultSet.getString(5);
		int year = resultSet.getInt(6);
		String location = resultSet.getString(7);
		String remark = resultSet.getString(8);
		String phone = resultSet.getString(9);

		return new CompanyDto(seq, name, size, industry, type, year, location, phone, remark);
	}

	private static String SEARCH_COMPANY_BY_NAME = "select seq, name, com_size, industry, com_type, year_founded, location, phone, remark from t_clientcompany where name like ?";
	public List<CompanyDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();
			List<CompanyDto> result = new ArrayList<CompanyDto>();

			while (resultSet.next()) {
				result.add(makeCompanyDto(resultSet));
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
	private static String GET_ALL = "select seq, name, com_size, industry, com_type, year_founded, location, phone, remark from t_clientcompany";
}
