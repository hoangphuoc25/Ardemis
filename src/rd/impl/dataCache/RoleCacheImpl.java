package rd.impl.dataCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.RoleDto;
import rd.spec.dataCache.RoleCache;

@Stateful
@SessionScoped
public class RoleCacheImpl implements RoleCache, Serializable {

	private static final long serialVersionUID = 8850072503399996985L;

	private Map<String, RoleDto> roleMap = new HashMap<String, RoleDto>();

	@Override
	public RoleDto getRole(String role) {
		return roleMap.get(role);
	}

	@Override
	public void putRole(RoleDto roleDto) {
		roleMap.put(roleDto.getRole(), roleDto);
	}

	public void clear(){
		roleMap.clear();
	}
}
