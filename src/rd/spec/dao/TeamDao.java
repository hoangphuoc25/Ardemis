package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.TeamDto;

public interface TeamDao {
	public void addTeam(Transaction transaction, TeamDto team) throws IOException;
	public void updateTeam(Transaction transaction, TeamDto team) throws IOException;
	public TeamDto getById(Transaction transaction, int seq) throws IOException;
	public void deleteTeam(Transaction transaction, TeamDto team) throws IOException;
	public List<TeamDto> getAll(Transaction transaction) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
}
