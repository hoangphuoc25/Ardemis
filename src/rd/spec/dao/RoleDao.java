package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.RoleDto;

public interface RoleDao {
	public List<RoleDto> getAllList(Transaction transaction) throws IOException;
	public void addRole(Transaction transaction, RoleDto roleDto) throws IOException;
	public void deleteRole(Transaction transaction, String role) throws IOException;
}
