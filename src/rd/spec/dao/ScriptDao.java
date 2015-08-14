package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ScriptDto;

public interface ScriptDao {
	public void addScript(Transaction transaction, ScriptDto script) throws IOException;
	public void editScript(Transaction transaction, ScriptDto script) throws IOException;
	public ScriptDto getById(Transaction transaction, int seq) throws IOException;
	public List<ScriptDto> getAll(Transaction transaction) throws IOException;
	public void deleteScript(Transaction transaction, int seq) throws IOException;
}
