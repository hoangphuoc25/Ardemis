package rd.spec.dataCache;

import rd.dto.ActivityDto;

public interface ActivityCache {
	public ActivityDto get(int seq);
	public void put(ActivityDto act);
	public void clear();
	public void remove(int seq);
}
