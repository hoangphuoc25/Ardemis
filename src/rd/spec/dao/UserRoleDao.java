package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.RoleDto;

public interface UserRoleDao {
	public List<RoleDto> getUserRoleList(Transaction transaction, String userId) throws IOException;
	public void addUserRole(Transaction transaction, String id, List<RoleDto> roleList) throws IOException;
	public void deleteUserRole(Transaction transaction, String id) throws IOException;
}
