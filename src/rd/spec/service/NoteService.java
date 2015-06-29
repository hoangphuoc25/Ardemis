package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.NoteDto;

public interface NoteService {
	public NoteDto getById(int seq) throws IOException;
	public void addNote(NoteDto note) throws IOException;
	public void deleteNote(int seq) throws IOException;
	public void updateNote(NoteDto note) throws IOException;
	public List<NoteDto> getBySender(String id) throws IOException;
	public List<NoteDto> getByReceiver(String id) throws IOException;
}
