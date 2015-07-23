package rd.spec.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.UserDto;

public interface UserDao {
	public List<UserDto> getUserList(Transaction transaction) throws IOException;
	public UserDto findUser(Transaction transaction, String id) throws IOException;
	public void addUser(Transaction transaction, UserDto userDto) throws IOException;
	public void updateUser(Transaction transaction, UserDto userDto) throws IOException;
	public void deleteUser(Transaction transaction, String id) throws IOException;
	public void updatePassword(Transaction transaction, String user, String password) throws IOException;
	public List<UserDto> getUserByRole(Transaction transaction, String role) throws IOException;
	public List<UserDto> getUserByTeam(Transaction transaction, int teamSeq) throws IOException;
	public List<UserDto> searchByName(Transaction transaction, String name) throws IOException;
	public UserDto searchByEmail(Transaction transaction, String email) throws IOException;
	public List<UserDto> getUserByTeamLazy(Transaction transaction, int seq) throws IOException;
	public void updateLoginTime(Transaction transaction, String userId, Date loginTime) throws IOException;
}
