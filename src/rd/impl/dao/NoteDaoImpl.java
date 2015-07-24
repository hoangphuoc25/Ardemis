package rd.impl.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.NoteDto;
import rd.dto.UserDto;
import rd.spec.dao.NoteDao;
import rd.spec.dao.Transaction;
import rd.spec.dao.UserDao;

public class NoteDaoImpl implements NoteDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	private UserDao userDao;

	@Inject
	public NoteDaoImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	public NoteDto getById(Transaction transaction, int seq) throws IOException {
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_ID);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();

			NoteDto result = null;
			while (resultSet.next()) {
				result = makeNoteDto(transaction, resultSet);
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
	private NoteDto makeNoteDto(Transaction transaction, ResultSet resultSet) throws IOException, SQLException {
		int seq = resultSet.getInt(1);
		UserDto sender = userDao.findUser(transaction, resultSet.getString(2));
		UserDto receiver = userDao.findUser(transaction, resultSet.getString(3));
		String content = resultSet.getString(4);
		Date createdDate = new Date(resultSet.getDate(5).getTime());
		String status = resultSet.getString(6);
		return new NoteDto(seq, sender, receiver, content, createdDate, status);
	}

	private static String GET_BY_ID = "select seq, from_user, to_user, content, created_date, status from t_note where seq=?";

	public void addNote(Transaction transaction, NoteDto note) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(ADD_NOTE);
			prepareStatement.setInt(1, getSeq(transaction));
			prepareStatement.setString(2, note.getFromUser().getId());
			prepareStatement.setString(3, note.getToUser().getId());
			prepareStatement.setString(4, note.getNote());
			prepareStatement.setDate(5, new java.sql.Date(note.getCreatedDate().getTime()));
			prepareStatement.setString(6, note.getStatus());

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

			int seq = 1;
			if (resultSet.next()) {
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

	private static String GET_SEQ = "select max(seq)+1 from t_note";
	private static String ADD_NOTE = "insert into t_note (seq, from_user, to_user, content, created_date, status) values (?, ?, ?, ?, ?, ?)";

	public void deleteNote(Transaction transaction, int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(DELETE_NOTE);
			prepareStatement.setInt(1, seq);
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
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
	private static String DELETE_NOTE = "delete from t_note where seq=?";

	public void updateNote(Transaction transaction, NoteDto note) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(UPDATE_NOTE);
			prepareStatement.setString(1, note.getToUser().getId());
			prepareStatement.setString(2, note.getNote());
			prepareStatement.setDate(3, new java.sql.Date(note.getCreatedDate().getTime()));
			prepareStatement.setString(4, note.getStatus());
			prepareStatement.setInt(5, note.getSeq());

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
	private static String UPDATE_NOTE = "update t_note set to_user=?, content=?, created_date=?, status=? where seq=?";
	public List<NoteDto> getBySender(Transaction transaction, String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_SENDER);
			prepareStatement.setString(1, id);
			resultSet = prepareStatement.executeQuery();

			List<NoteDto> result = new ArrayList<NoteDto>();
			while (resultSet.next()) {
				result.add(makeNoteDto(transaction, resultSet));
			}
			logger.error(""+result.size());
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
	private static String GET_BY_SENDER = "select seq, from_user, to_user, content, created_date, status from t_note where from_user=?";
	public List<NoteDto> getByReceiver(Transaction transaction, String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_BY_RECEIVER);
			prepareStatement.setString(1, id);
			resultSet = prepareStatement.executeQuery();

			List<NoteDto> result = new ArrayList<NoteDto>();
			while (resultSet.next()) {
				result.add(makeNoteDto(transaction, resultSet));
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
	private static String GET_BY_RECEIVER = "select seq, from_user, to_user, content, created_date, status from t_note where to_user=?";
	public List<NoteDto> getNoteByStatus(Transaction transaction, String userId, String status) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {
			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_NOTE_BY_STATUS);
			prepareStatement.setString(1, userId);
			prepareStatement.setString(2, status);
			resultSet = prepareStatement.executeQuery();

			List<NoteDto> result = new ArrayList<NoteDto>();
			while (resultSet.next()) {
				result.add(makeNoteDto(transaction, resultSet));
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
	private static String GET_NOTE_BY_STATUS = "select seq, from_user, to_user, content, created_date, status from t_note where to_user=? and status=?";
	public List<NoteDto> getRecentNote(Transaction transaction, String userId) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		try {

			Calendar date = new GregorianCalendar();
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			date.add(Calendar.DAY_OF_MONTH, -1);

			Connection connection = transaction.getResource(Connection.class);
			prepareStatement = connection.prepareStatement(GET_RECENT_NOTE);
			prepareStatement.setString(1, userId);
			prepareStatement.setDate(2, new java.sql.Date(date.getTime().getTime()));
			resultSet = prepareStatement.executeQuery();

			List<NoteDto> result = new ArrayList<NoteDto>();
			while (resultSet.next()) {
				result.add(makeNoteDto(transaction, resultSet));
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
	private static String GET_RECENT_NOTE = "select seq, from_user, to_user, content, created_date, status from t_note where to_user=? and created_date >= ?";
}
