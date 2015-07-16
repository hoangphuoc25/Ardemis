package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import rd.dto.CompanyDto;
import rd.dto.MeetingDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CompanyService;
import rd.spec.service.MeetingService;
import rd.utils.Pair;

@Named
@ConversationScoped
public class ScheduleController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject MeetingService meetingService;
	@Inject SessionManager sessionManager;
	@Inject CompanyService compService;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public ScheduleModel getModel() throws IOException {
		System.out.println("ScheduleController.getModel()");
		if (model == null) {
			model = new DefaultScheduleModel();
			for (MeetingDto dto: getEvents()) {
				model.addEvent(new DefaultScheduleEvent(dto.getDetail(), dto.getFrom(), dto.getTo()));
			}
		}
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	private Date from;
	private Date to;
	private String title;
	private ScheduleModel model;
	private String companyName;

	public void addNewEvent() throws IOException {
		reload();
		if (to.getTime() < from.getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid date info", null);
			return;
		}
		CompanyDto comp = compService.getById(Integer.parseInt(companyName.split("[()]")[1]));
		System.out.println(Integer.parseInt(companyName.split("[()]")[1]));
		MeetingDto newMeeting = new MeetingDto(meetingService.getSeq(), from, to, title, comp, sessionManager.getLoginUser());
		meetingService.addMeeting(newMeeting);
		if (model == null) {
			System.out.println("model is null");
		}

		model.addEvent(new DefaultScheduleEvent(title, from, to));
		events.add(newMeeting);

		sessionManager.addGlobalMessageInfo("New schedule event added", null);
		if (!checkConflict()) {
			sessionManager.addGlobalMessageWarn("Warning: Conflict detected in your chedule.", null);
		}
		from = null;
		to = null;
		title = "";
		companyName = "";

	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public boolean checkConflict() throws IOException {
		List<MeetingDto> temp = meetingService.getMeetingByUser(sessionManager.getLoginUser().getId());
		List<Pair<Date, Integer>> time = new ArrayList<Pair<Date, Integer>>();
		for (MeetingDto dto: temp) {
			time.add(new Pair<Date, Integer>(dto.getFrom(), 1));
			time.add(new Pair<Date, Integer>(dto.getTo(), 2));
		}
		Collections.sort(time, new Comparator<Pair<Date, Integer>>() {
			public int compare(Pair<Date, Integer> first, Pair<Date, Integer> second) {
				if (first.getFirst().compareTo(second.getFirst()) != 0) {
					return first.getFirst().compareTo(second.getFirst());
				} else {
					return first.getSecond().compareTo(second.getSecond());
				}
			}
		});
		int count = 0;
		for (Pair<Date, Integer> p: time) {
			System.out.println(p.getFirst() + " " + p.getSecond());
		}
		for (int i = 0; i < time.size(); i++) {
			Pair<Date, Integer> p = time.get(i);
			if (p.getSecond() == 1) {
				count++;
			} else {
				count --;
			}
			if (count == 2) {
				for (int k = i + 1; k < time.size(); k++) {
					if (time.get(k).getFirst().compareTo(p.getFirst()) == 0 && time.get(k).getSecond() == 2) {
						break;
					} else if (time.get(k).getFirst().compareTo(p.getFirst()) > 0) {
						System.out.println("false here : " +time.get(k).getFirst());
						return false;
					}
				}
			}
		}
		return true;
	}

	public String logout() {
		conversationEnd();
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}

	public List<MeetingDto> getEvents() throws IOException {
		if (events == null) {
			events = meetingService.getMeetingByUser(sessionManager.getLoginUser().getId());
		}
		return events;
	}

	public void setEvents(List<MeetingDto> events) {
		this.events = events;
	}

	private List<MeetingDto> events;

	public void remove(MeetingDto meeting) throws IOException {
		for (int i = events.size() - 1; i >= 0; i--) {
			if (events.get(i).getSeq() == meeting.getSeq()) {
				events.remove(i);
				meetingService.deleteMeeting(meeting.getSeq());
				break;
			}
		}
		model = new DefaultScheduleModel();
		for (MeetingDto dto: events) {
			model.addEvent(new DefaultScheduleEvent(dto.getDetail(), dto.getFrom(), dto.getTo()));
		}
		sessionManager.addGlobalMessageInfo("Event removed.", null);
	}

}
