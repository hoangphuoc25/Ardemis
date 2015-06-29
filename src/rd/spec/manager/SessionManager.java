package rd.spec.manager;

import java.io.IOException;

import rd.dto.UserDto;

public interface SessionManager {
	public UserDto getLoginUser() throws IOException;
	public boolean hasUserRole(String role);
	public void logoff();
	public void redirect(String url)  throws IOException;
	public void addGlobalMessageFatal(String summary, String detail);
	public void addGlobalMessageWarn(String summary, String detail);
	public void addGlobalMessageInfo(String summary, String detail);
}
