package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class ManagerController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject UserService userService;
	@Inject SessionManager sessionManager;

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if(!conversation.isTransient()){
			conversation.end();
		}
	}

	public void reload() {
		conversationBegin();
	}

	public List<UserDto> getTeamRoster() throws IOException {
		if (team >= 0) {
			teamRoster = userService.getUserByTeamLazy(team);
		}
		return teamRoster;
	}

	public void setTeamRoster(List<UserDto> teamRoster) {
		this.teamRoster = teamRoster;
	}

	public UserDto getSelectedSale() {
		return selectedSale;
	}

	public void setSelectedSale(UserDto selectedSale) {
		this.selectedSale = selectedSale;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	private List<UserDto> teamRoster;
	private UserDto selectedSale;
	private int team;

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}

	public String link(UserDto sale) {
		conversationEnd();
		return "../faces/employee.jsf?id="+sale.getId()+"&faces-redirect=true";
	}
}
