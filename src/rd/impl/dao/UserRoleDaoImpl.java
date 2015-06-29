package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.RoleDto;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserRoleDao;

//@Stateless
public class UserRoleDaoImpl implements UserRoleDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<RoleDto> getUserRoleList(Transaction transaction, String userId) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_USER_ROLL);
			prepareStatement.setString(1, userId);
			resultSet = prepareStatement.executeQuery();

			List<RoleDto> items = new ArrayList<RoleDto>();
			while (resultSet.next()) {
				RoleDto roleDto = makeRoleDto(transaction, resultSet);
				items.add(roleDto);
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

	private RoleDto makeRoleDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		String role = resultSet.getString(1);

		RoleDto roleDto = new RoleDto(role);
		return roleDto;
	}

	@Override
	public void addUserRole(Transaction transaction, String id, List<RoleDto> roleList)
			throws IOException {
		PreparedStatement prepareStatement = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_USER_ROLE);
			int count = 0;
			for (RoleDto role : roleList) {
				prepareStatement.setString(1, id);
				prepareStatement.setString(2, role.getRole());
				prepareStatement.addBatch();
				count++;
				if(count > 90){
					prepareStatement.executeBatch();
					count = 0;
				}
			}
			if(count > 0){
				prepareStatement.executeBatch();
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
	public void deleteUserRole(Transaction transaction, String userId)
			throws IOException {
		PreparedStatement prepareStatement = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_USER_ROLL);
			prepareStatement.setString(1, userId);
			prepareStatement.executeUpdate();
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

	private static final String GET_USER_ROLL = "select rolename from s_userrole where id = ? order by rolename";
	private static final String ADD_USER_ROLE = "insert into s_userrole (id, rolename) values (?, ?)";
	private static final String DELETE_USER_ROLL = "delete from s_userrole where id = ?";

}
