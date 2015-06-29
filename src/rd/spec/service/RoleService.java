package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.RoleDto;

public interface RoleService {

	public List<RoleDto> getAllList() throws IOException;
}
