package rd.spec.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.UserDto;

public interface UserService {
	public UserDto findUserById(String id) throws IOException;
	public void addUser(UserDto userDto) throws IOException;
	public void updateUser(UserDto userDto) throws IOException;
	public void deleteUser(String id) throws IOException;
	public List<UserDto> getUserByTeam(int teamSeq) throws IOException;
	public List<UserDto> searchByName(String name) throws IOException;
	public void updatePassword(String user, String password) throws IOException;
	public List<UserDto> getUserByRole(String role) throws IOException;
	public UserDto searchByEmail(String email) throws IOException;
	public List<UserDto> getUserByTeamLazy(int seq) throws IOException;
	public void updateLoginTime(String userId, Date loginTime) throws IOException;
}
