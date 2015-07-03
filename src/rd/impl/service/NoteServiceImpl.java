package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.NoteDto;
import rd.spec.dao.NoteDao;
import rd.spec.dao.Transaction;
import rd.spec.service.NoteService;

public class NoteServiceImpl implements NoteService, Serializable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = -1L;

	private Transaction transaction;
	private NoteDao noteDao;

	@Inject
	public NoteServiceImpl(Transaction transaction, NoteDao noteDao) {
		this.setTransaction(transaction);
		this.setNoteDao(noteDao);
	}

	public NoteDto getById(int seq) throws IOException {
		try {
			getTransaction().begin();
			NoteDto note = getNoteDao().getById(getTransaction(), seq);
			getTransaction().commit();
			return note;
		} catch (IOException e) {
			getTransaction().rollback();
			throw e;
		}
	}

	public void addNote(NoteDto note) throws IOException {
		try {
			getTransaction().begin();
			getNoteDao().addNote(getTransaction(), note);
			getTransaction().commit();
		} catch (IOException e) {
			getTransaction().rollback();
			throw e;
		}
	}

	public void deleteNote(int seq) throws IOException {
		try {
			getTransaction().begin();
			getNoteDao().deleteNote(getTransaction(), seq);
			getTransaction().commit();
		} catch (IOException e) {
			getTransaction().rollback();
			throw e;
		}
	}

	public void updateNote(NoteDto note) throws IOException {
		try {
			getTransaction().begin();
			getNoteDao().updateNote(getTransaction(), note);
			getTransaction().commit();
		} catch (IOException e) {
			getTransaction().rollback();
			throw e;
		}
	}

	public List<NoteDto> getBySender(String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			getTransaction().begin();
			List<NoteDto> result = getNoteDao().getBySender(getTransaction(), id);
			getTransaction().commit();
			return result;
		} catch (IOException e) {
			getTransaction().rollback();
			throw e;
		}
	}

	public List<NoteDto> getByReceiver(String id) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			getTransaction().begin();
			List<NoteDto> result = getNoteDao().getByReceiver(getTransaction(), id);
			getTransaction().commit();
			return result;
		} catch (IOException e) {
			getTransaction().rollback();
			throw e;
		}
	}

	public NoteDao getNoteDao() {
		return noteDao;
	}

	public void setNoteDao(NoteDao noteDao) {
		this.noteDao = noteDao;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public int getSeq() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			int seq = noteDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
