package rd.impl.dataCache;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.CompanyDto;
import rd.dto.TeamDto;
import rd.spec.dataCache.TeamCache;

@Stateful
@SessionScoped
public class TeamCacheImpl implements TeamCache, Serializable {

	private static final long serialVersionUID = 8850072503399996985L;
	private Map<Integer, TeamDto> map = new HashMap<Integer, TeamDto>();

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public TeamDto getTeam(int seq) throws IOException {
		if (map.containsKey(seq))
			return map.get(seq);
		else
			return null;
	}

	@Override
	public void put(TeamDto team) {
		map.put(team.getSeq(), team);
	}

	@Override
	public void remove(int seq) {
		map.remove(seq);
	}

}
