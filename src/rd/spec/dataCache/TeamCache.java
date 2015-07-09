package rd.spec.dataCache;

import java.io.IOException;

import rd.dto.TeamDto;

public interface TeamCache {
	public void clear();
	public TeamDto getTeam(int seq) throws IOException;
	public void put(TeamDto team);
	public void remove(int seq);
}
