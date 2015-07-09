package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.TeamDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.TeamService;
import rd.utils.DatabaseUtil;

@Named
@ConversationScoped
public class WelcomeController implements Serializable {
	/**
	 *
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;

	private List<TeamDto> teams;

	@Inject SessionManager sessionMan;
	@Inject DatabaseUtil dbCache;
	@Inject TeamService teamService;
	@Inject Conversation conversation;

	private TeamDto newTeam = new TeamDto();

	private boolean addMode = false;

	public List<TeamDto> getTeams() throws IOException {
		if (teams == null || teams.size() == 0) {
			teams = teamService.getAll();
		}
		return teams;
	}

	public void setTeams(List<TeamDto> teams) {
		this.teams = teams;
	}

	public void reload() {
		conversationBegin();
		System.out.println("CONVERSATION BEGAN");
	}

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

	public void def(TeamDto team) {
		reload();
		team.setSeq(4);
	}

	public void startAdd() {
		reload();
		newTeam = new TeamDto();
		addMode = true;
		logger.error("ADDMODE: " + addMode);
		logger.error("WelcomeController.startAdd()");
		// reload();
	}

	public TeamDto getNewTeam() {
		return newTeam;
	}

	public void setNewTeam(TeamDto newTeam) {
		this.newTeam = newTeam;
	}

	public void addNewTeam() throws IOException, ValidatorException {
		logger.error("newteam name: "+newTeam.getName());
		if (newTeam.getName().isEmpty()) {
			sessionMan.addGlobalMessageFatal("INVALID INFO PROVIDED", null);
			logger.error("invalid info. error messsage added.");
			addMode = true;
			return;
		}
		System.out.println("out of if");
		newTeam.setSeq(teamService.getSeq());
		System.out.println("inserted");
		teamService.addTeam(newTeam);
		System.out.println("added to db");
		getTeams().add(newTeam);
		System.out.println("added to teams");
		newTeam = new TeamDto();
		System.out.println("reset ");
		addMode = false;
		System.out.println("set addmode=false");

		sessionMan.addGlobalMessageInfo("ADDED, addmode=false", null);
		logger.error("WelcomeController.addNewTeam()");
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}
}
