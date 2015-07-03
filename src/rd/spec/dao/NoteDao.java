package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.NoteDto;

public interface NoteDao {
	public NoteDto getById(Transaction transaction, int seq) throws IOException;
	public void addNote(Transaction transaction, NoteDto note) throws IOException;
	public void deleteNote(Transaction transaction, int seq) throws IOException;
	public void updateNote(Transaction transaction, NoteDto note) throws IOException;
	public List<NoteDto> getBySender(Transaction transaction, String id) throws IOException;
	public List<NoteDto> getByReceiver(Transaction transaction, String id) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
}
