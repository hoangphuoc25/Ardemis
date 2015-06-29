package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.NoteDto;
import rd.spec.dao.NoteDao;
import rd.spec.dao.Transaction;
import rd.spec.service.NoteService;

public class NoteServiceImpl implements NoteService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private NoteDao noteDao;

	@Inject
	public NoteServiceImpl(Transaction transaction, NoteDao noteDao) {
		this.transaction = transaction;
		this.noteDao = noteDao;
	}

	public NoteDto getById(int seq) throws IOException {
		try {
			transaction.begin();
			NoteDto note = noteDao.getById(transaction, seq);
			transaction.commit();
			return note;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addNote(NoteDto note) throws IOException {
		try {
			transaction.begin();
			noteDao.addNote(transaction, note);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteNote(int seq) throws IOException {
		try {
			transaction.begin();
			noteDao.deleteNote(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateNote(NoteDto note) throws IOException {
		try {
			transaction.begin();
			noteDao.updateNote(transaction, note);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<NoteDto> getBySender(String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<NoteDto> result = noteDao.getBySender(transaction, id);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<NoteDto> getByReceiver(String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<NoteDto> result = noteDao.getByReceiver(transaction, id);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
