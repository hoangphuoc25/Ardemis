package rd.dto;

public class RoleDto {
	private String role;

	//You must declared default constructor for Framework.
	public RoleDto(){
		super();
	}

	public RoleDto(String role){
		this.role = role;
	}

	public String getRole() {
		return role;
	}

}
