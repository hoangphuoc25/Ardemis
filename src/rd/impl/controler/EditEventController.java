package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.CompanyDto;
import rd.dto.MeetingDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CompanyService;
import rd.spec.service.MeetingService;

@Named
@ConversationScoped
public class EditEventController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject CompanyService compService;
	@Inject MeetingService meetingService;
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

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "("+dto.getSeq()+")");
		}
		return result;
	}

	public MeetingDto getEvent() throws IOException {
		if (seq != 0) {
			event = meetingService.getById(seq);
			companyName = event.getCustomer().getName() + "(" + event.getCustomer().getSeq()+ ")";
		}
		return event;
	}

	public void setEvent(MeetingDto event) {
		this.event = event;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	private MeetingDto event;
	private int seq;
	private String companyName;
	private Date from;
	private Date to;

	public void submit() throws IOException {
		event.setFrom(from);
		event.setTo(to);
		if (event.getTo().getTime() < event.getFrom().getTime()) {
			sessionManager.addGlobalMessageFatal("Invalid time", null);
			return;
		}
		if (event.getDetail().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Please enter detail of meeting", null);
			return;
		}
		int cusSeq = Integer.parseInt(companyName.split("[()]")[1]);
		CompanyDto cust = compService.getById(cusSeq);
		event.setCustomer(cust);

		meetingService.editMeeting(event);

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("salesperson.jsf");
	}

	public void cancel() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("salesperson.jsf");
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getFrom() throws IOException {
		if (from == null) {
			from = getEvent().getFrom();
		}
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() throws IOException {
		if (to == null) {
			to = getEvent().getTo();
		}
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}
}
