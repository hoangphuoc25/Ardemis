package rd.spec.dataCache;

import rd.dto.UserDto;

public interface UserCache {
	public UserDto getUser(String id);
	public void put(UserDto user);
	public void remove(UserDto user);
	public void clear();
}
