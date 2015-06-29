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

import rd.dto.TeamDto;
import rd.spec.dao.TeamDao;
import rd.spec.dao.Transaction;
import rd.spec.dataCache.RoleCache;
import rd.utils.DatabaseUtil;

public class TeamDaoImpl implements TeamDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RoleCache roleCache;
	private DatabaseUtil cache;

	@Inject
	public TeamDaoImpl(RoleCache roleCache, DatabaseUtil cache) {
		this.roleCache = roleCache;
		this.cache = cache;
	}

	public int getSeq(Transaction transaction) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_SEQ);
			resultSet = prepareStatement.executeQuery();

			int result = 1;
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

	public void addTeam(Transaction transaction, TeamDto team) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_TEAM);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, team.getName());
			prepareStatement.setString(3, team.getRemark());

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

	private static String GET_SEQ = "select max(seq)+1 from t_saleteam";
	private static String ADD_TEAM = "insert into t_saleteam (seq, name, remark) values (?, ?, ?)";
	private static String UPDATE_TEAM = "update t_saleteam set name=?, remark=? where seq=?";
	private static String GET_BY_ID = "select seq, name, remark from t_saleteam where seq=?";
	private static String DELETE_TEAM = "delete from t_saleteam where seq=?";
	private static String GET_ALL = "select seq, name, remark from t_saleteam";

	public void updateTeam(Transaction transaction, TeamDto team) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_TEAM);
			prepareStatement.setString(1, team.getName());
			prepareStatement.setString(2, team.getRemark());
			prepareStatement.setInt(3, team.getSeq());

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

	public TeamDto getById(Transaction transaction, int seq) throws IOException {
		if (cache.getTeamList().containsKey(seq)) {
			logger.error("USING CACHE");
			return cache.getTeamList().get(seq);
		}

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			TeamDto team = null;
			while (resultSet.next()) {
				team = makeTeamDto(resultSet);
			}

			return team;
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

	private TeamDto makeTeamDto(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt(1);
		String name = resultSet.getString(2);
		String remark = resultSet.getString(3);
		return new TeamDto(id, name, remark);
	}

	public void deleteTeam(Transaction transaction, TeamDto team) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_TEAM);
			prepareStatement.setInt(1, team.getSeq());
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

	public List<TeamDto> getAll(Transaction transaction) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_ALL);
			resultSet = prepareStatement.executeQuery();

			List<TeamDto> result = new ArrayList<TeamDto>();
			while (resultSet.next()) {
				result.add(makeTeamDto(resultSet));
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
}
