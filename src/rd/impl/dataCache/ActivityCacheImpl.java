package rd.impl.dataCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.ActivityDto;
import rd.dto.CompanyDto;
import rd.spec.dataCache.ActivityCache;

@Stateful
@SessionScoped

public class ActivityCacheImpl implements Serializable, ActivityCache {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, ActivityDto> actMap = new HashMap<Integer, ActivityDto>();


	@Override
	public ActivityDto get(int seq) {
		if (actMap.containsKey(seq)) {
			return actMap.get(seq);
		} else {
			return null;
		}
	}

	@Override
	public void put(ActivityDto act) {
		actMap.put(act.getSeq(), act);
	}

	@Override
	public void clear() {
		actMap.clear();
	}

	@Override
	public void remove(int seq) {
		actMap.remove(seq);
	}

}
