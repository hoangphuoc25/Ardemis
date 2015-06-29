package rd.spec.dataCache;

import rd.dto.RoleDto;

public interface RoleCache {
	public RoleDto getRole(String role);
	public void putRole(RoleDto roleDto);
	public void clear();
}
