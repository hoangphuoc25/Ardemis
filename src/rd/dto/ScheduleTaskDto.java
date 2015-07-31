package rd.dto;

import java.util.Date;

public class ScheduleTaskDto {
	private int seq;
	private String category;
	private CompanyDto customer;
	private Date time;
	private String username;

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

	public ScheduleTaskDto(int seq, String category, CompanyDto customer,
			Date time, String username, String detail) {
		this.seq = seq;
		this.category = category;
		this.customer = customer;
		this.time = time;
		this.username = username;
		this.detail = detail;
	}

	public ScheduleTaskDto() {
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

	private String detail;
}
