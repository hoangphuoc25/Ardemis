package rd.faces;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import rd.dto.RoleDto;

public class RoleListModel extends ListDataModel<RoleDto> implements SelectableDataModel<RoleDto> {

	public RoleListModel(List<RoleDto> roleList){
		super(roleList);
	}

	@Override
	public RoleDto getRowData(String rowKey) {
		String role = rowKey;
		List<RoleDto> roleList = (List<RoleDto>)getWrappedData();
		for (RoleDto roleDto : roleList) {
			if(roleDto.getRole().equals(role)){
				return roleDto;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(RoleDto roleDto) {
		return roleDto.getRole();
	}

}
