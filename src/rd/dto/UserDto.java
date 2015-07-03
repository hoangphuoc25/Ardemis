package rd.dto;

import java.util.List;

public class UserDto {

	private String id;
	private String password;

	private String name;
	private List<RoleDto> roles;
	private String email;
	private String phone;
	private TeamDto team;

	public UserDto() {
		this.id = "";
		this.password = "";
		this.name = "";
		this.email = "";
		this.phone = "";
		this.team = new TeamDto();
	}

	public UserDto(String id, String password, String name, List<RoleDto> roles, String email, String phone, TeamDto team) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.roles = roles;
		this.email = email;
		this.phone = phone;
		this.team = team;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoleDto> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public TeamDto getTeam() {
		return team;
	}

	public void setTeam(TeamDto team) {
		this.team = team;
	}

}
