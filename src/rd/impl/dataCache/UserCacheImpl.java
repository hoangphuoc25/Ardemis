package rd.impl.dataCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.UserDto;
import rd.spec.dataCache.UserCache;

@Stateful
@SessionScoped
public class UserCacheImpl implements UserCache, Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, UserDto> userMap = new HashMap<String, UserDto>();

	@Override
	public UserDto getUser(String id) {
		if (userMap.containsKey(id)) {
			return userMap.get(id);
		} else {
			return null;
		}
	}

	@Override
	public void put(UserDto user) {
		userMap.put(user.getId(), user);
	}

	@Override
	public void remove(UserDto user) {
		userMap.remove(user.getId());
	}

	@Override
	public void clear() {
		userMap.clear();
	}
}
