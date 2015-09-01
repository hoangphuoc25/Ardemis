package rd.dto;

import java.util.Date;

public class ScheduleTaskDto {
	private int seq;
	private String category;
	private CompanyDto customer;
	private Date time;
	private String username;
	private ContactDto contact;
	private int actId;
	private String status;
	private boolean selected;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public CompanyDto getCustomer() {
		return customer;
	}

	public void setCustomer(CompanyDto customer) {
		this.customer = customer;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public ScheduleTaskDto(int seq, String category, Date time, String username, String detail, ContactDto contact, int actId, String status) {
		this.seq = seq;
		this.category = category;
		this.time = time;
		this.username = username;
		this.detail = detail;
		this.contact = contact;
		this.actId = actId;
		this.status = status;
	}

	public ScheduleTaskDto() {
		this.status = "Pending";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	private String detail;
}
