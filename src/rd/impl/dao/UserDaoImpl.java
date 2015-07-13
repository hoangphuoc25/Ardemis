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

import rd.dto.RoleDto;
import rd.dto.TeamDto;
import rd.dto.UserDto;
import rd.spec.dao.TeamDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;
import rd.spec.dao.UserRoleDao;
import rd.utils.PasswordHash;

public class UserDaoImpl implements UserDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private TeamDao teamDao;
	private UserRoleDao userRoleDao;

	@Inject
	public UserDaoImpl(TeamDao teamDao, UserRoleDao userRoleDao) {
		this.teamDao = teamDao;
		this.userRoleDao = userRoleDao;
	}

	@Override
	public List<UserDto> getUserList(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<UserDto> items = new ArrayList<UserDto>();
			while (resultSet.next()) {
				UserDto userDto = makeUserDto(transaction, resultSet);
				items.add(userDto);
			}
			return items;
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

	private UserDto makeUserDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		String id = resultSet.getString(1);
		String name = resultSet.getString(2);
		String email = resultSet.getString(3);
		String phone = resultSet.getString(4);
		TeamDto team = teamDao.getById(transaction, resultSet.getInt(5));
		List<RoleDto> roles = userRoleDao.getUserRoleList(transaction, id);

		UserDto userDto = new UserDto(id, "", name, roles, email, phone, team);
		return userDto;
	}

	@Override
	public UserDto findUser(Transaction transaction, String id)
			throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setString(1, id);
			resultSet = prepareStatement.executeQuery();

			UserDto result = null;
			if(resultSet.next()) {
				result = makeUserDto(transaction, resultSet);
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

	@Override
	public void addUser(Transaction transaction, UserDto userDto)
			throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(INSERT_USER);
			prepareStatement.setString(1, userDto.getId());
			prepareStatement.setString(2, PasswordHash.makePasswordHash(userDto.getPassword()));
			prepareStatement.setString(3, userDto.getName());
			prepareStatement.setString(4, userDto.getEmail());
			prepareStatement.setString(5, userDto.getPhone());
			prepareStatement.setInt(6, userDto.getTeam().getSeq());
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

	@Override
	public void updateUser(Transaction transaction, UserDto userDto) throws IOException {
		PreparedStatement prepareStatement = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_USER);
			prepareStatement.setString(1, userDto.getName());
			// prepareStatement.setString(2, PasswordHash.makePasswordHash(emp.getPassword()));
			prepareStatement.setString(2, userDto.getEmail());
			prepareStatement.setString(3, userDto.getPhone());
			prepareStatement.setInt(4, userDto.getTeam().getSeq());
			prepareStatement.setString(5, userDto.getId());
			prepareStatement.executeUpdate();

			userRoleDao.deleteUserRole(transaction, userDto.getId());

			if (userDto.getRoles().size() > 0){
				userRoleDao.addUserRole(transaction, userDto.getId(), userDto.getRoles());
			}

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (prepareStatement != null) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}

	}

	@Override
	public void deleteUser(Transaction transaction, String id) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_USER);
			prepareStatement.setString(1, id);
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

	public List<UserDto> getUserByTeam(Transaction transaction, int teamSeq) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_USER_BY_TEAM);
			prepareStatement.setInt(1, teamSeq);
			resultSet = prepareStatement.executeQuery();

			List<UserDto> result = new ArrayList<UserDto>();
			while (resultSet.next()) {
				result.add(makeUserDto(transaction, resultSet));
			}
			logger.error("result: " + result.size());
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

	public List<UserDto> searchByName(Transaction transaction, String name) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_BY_NAME);
			prepareStatement.setString(1, "%" + name + "%");
			resultSet = prepareStatement.executeQuery();
			List<UserDto> items = new ArrayList<UserDto>();
			while (resultSet.next()) {
				UserDto userDto = makeUserDto(transaction, resultSet);
				items.add(userDto);
			}
			return items;
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

	private static String SEARCH_BY_NAME = "select id, name, email, phone, team_seq from s_users where name like ?";
	private static String GET_USER_BY_TEAM = "select id, name, email, phone, team_seq from s_users where team_seq=?";
	private static String GET_ALL = "select id, name, email, phone, team_seq from s_users order by id";
	private static String GET_BY_ID = "select id, name, email, phone, team_seq from s_users where id=?";
	private static String DELETE_USER = "delete from s_users where id=?";
	private static String UPDATE_USER = "update s_users set name=?, email=?, phone=?, team_seq=? where id=?";
	private static String INSERT_USER = "insert into s_users (id, password, name, email, phone, team_seq) values (?, ?, ?, ?, ?, ?)";
	private static String UPDATE_PASSWORD = "update s_users set password=? where id=?";

	public void updatePassword(Transaction transaction, String user, String password) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_PASSWORD);
			prepareStatement.setString(1, PasswordHash.makePasswordHash(password));
			prepareStatement.setString(2, user);
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
	public List<UserDto> getUserByRole(Transaction transaction, String role) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_USER_BY_ROLE);
			prepareStatement.setString(1, role);
			resultSet = prepareStatement.executeQuery();

			List<UserDto> result = new ArrayList<UserDto>();
			while (resultSet.next()) {
				result.add(makeUserDto(transaction, resultSet));
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
	private static String GET_USER_BY_ROLE = "select u.id from s_users u join s_userrole ur on u.id=ur.id where ur.rolename = ?";
	public UserDto searchByEmail(Transaction transaction, String email) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(SEARCH_BY_EMAIL);
			prepareStatement.setString(1, email);
			resultSet = prepareStatement.executeQuery();

			UserDto result = new UserDto();
			while (resultSet.next()) {
				result = makeUserDto(transaction, resultSet);
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
	private static String SEARCH_BY_EMAIL = "select id, name, email, phone, team_seq, from s_users where email=?";
	public List<UserDto> getUserByTeamLazy(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_USER_BY_TEAM_LAZY);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			List<UserDto> all = new ArrayList<UserDto>();
			while (resultSet.next()) {
				all.add(makeUserDtoLazy(resultSet));
			}
			return all;
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
	private UserDto makeUserDtoLazy(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString(1);
		String name = resultSet.getString(2);
		String email = resultSet.getString(3);
		String phone = resultSet.getString(4);

		UserDto userDto = new UserDto(id, "", name, null, email, phone, null);
		return userDto;
	}

	private static String GET_USER_BY_TEAM_LAZY = "select id, name, email, phone, team_seq from s_users where team_seq=?";
}
