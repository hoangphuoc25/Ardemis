package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ScriptDto;

public interface ScriptService {
	public void addScript(ScriptDto script) throws IOException;
	public void editScript(ScriptDto script) throws IOException;
	public ScriptDto getById(int seq) throws IOException;
	public List<ScriptDto> getAll() throws IOException;
	public void deleteScript(int seq) throws IOException;
}
