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
import rd.spec.dao.RoleDao;
import rd.spec.dao.Transaction;
import rd.spec.dataCache.RoleCache;

//@Stateless
public class RoleDaoImpl implements RoleDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RoleCache roleCache;

	@Inject
	public RoleDaoImpl(RoleCache roleCache){
		this.roleCache = roleCache;
	}

	@Override
	public List<RoleDto> getAllList(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			roleCache.clear();
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<RoleDto> items = new ArrayList<RoleDto>();
			while (resultSet.next()) {
				RoleDto roleDto = makeBookDto(transaction, resultSet);
				items.add(roleDto);
				roleCache.putRole(roleDto);
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

	private RoleDto makeBookDto(Transaction transaction, ResultSet resultSet) throws SQLException, IOException {
		String role = resultSet.getString(1);

		RoleDto roleDto = new RoleDto(role);
		return roleDto;
	}

	@Override
	public void addRole(Transaction transaction, RoleDto roleDto) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRole(Transaction transaction, String role) throws IOException {
		// TODO Auto-generated method stub

	}

	private static final String GET_ALL = "select role from s_role order by role";

}
