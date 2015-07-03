package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.TeamDto;

public interface TeamService {
	public void addTeam(TeamDto team) throws IOException;
	public void updateTeam(TeamDto team) throws IOException;
	public TeamDto getById(int seq) throws IOException;
	public void deleteTeam(TeamDto team) throws IOException;
	public List<TeamDto> getAll() throws IOException;
	public int getSeq() throws IOException;
}
