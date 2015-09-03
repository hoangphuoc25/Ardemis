package rd.dto;

import java.util.Date;

public class MeetingDto {
	private int seq;
	private Date from;
	private Date to;
	private String detail;
	private CompanyDto customer;
	private UserDto salesperson;
	private String location;
	private ContactDto contact;
	private int actId;
	private boolean selected;
	private String status;
	private String estimatedTravelTime;

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public CompanyDto getCustomer() {
		return customer;
	}

	public void setCustomer(CompanyDto customer) {
		this.customer = customer;
	}

	public MeetingDto(int seq, Date from, Date to, String detail, ContactDto contact, UserDto sale, String location, int actId) {
		this.seq = seq;
		this.from = from;
		this.to = to;
		this.detail = detail;
		this.salesperson = sale;
		this.location = location;
		this.contact = contact;
		this.setActId(actId);
	}

	public MeetingDto() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public UserDto getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(UserDto salesperson) {
		this.salesperson = salesperson;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEstimatedTravelTime() {
		return estimatedTravelTime;
	}

	public void setEstimatedTravelTime(String estimatedTravelTime) {
		this.estimatedTravelTime = estimatedTravelTime;
	}
}
