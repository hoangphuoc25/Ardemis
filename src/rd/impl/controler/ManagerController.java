package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import rd.dto.MeetingDto;
import rd.dto.UserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.MeetingService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class ManagerController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject UserService userService;
	@Inject SessionManager sessionManager;
	@Inject MeetingService meetingService;

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

	public List<MeetingDto> getEventList() throws IOException {
		if (eventList == null || eventList.size() == 0) {
			eventList = meetingService.getMeetingToday();
		}
		return eventList;
	}

	public void setEventList(List<MeetingDto> eventList) {
		this.eventList = eventList;
	}

	private List<MeetingDto> eventList;

	public void viewSchedule(UserDto sale) throws IOException {
		conversationEnd();

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("employeeSchedule.jsf?id="+sale.getId());
	}

	private ScheduleModel model;

	public ScheduleModel getModel() throws IOException {

			model = new DefaultScheduleModel();
			List<MeetingDto> temp = meetingService.getMeetingByUser(empId);
			System.out.println(empId);
			System.out.println(temp.size());
			for (MeetingDto dto: temp)
				model.addEvent(new DefaultScheduleEvent(dto.getDetail(), dto.getFrom(), dto.getTo()));

		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	private String empId;

	public void updateEmpId(UserDto sale) {
		this.empId = sale.getId();
		System.out.println(empId);
	}
}
