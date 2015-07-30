package rd.dto;

import java.util.Date;

public class ActivityDto {
	private int seq;
	private CompanyDto customer;
	private Date startDate;
	private String status;
	private String remark;
	private UserDto salesperson;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public CompanyDto getCustomer() {
		return customer;
	}

	public void setCustomer(CompanyDto customer) {
		this.customer = customer;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ActivityDto(int seq, CompanyDto customer, Date startDate, String status, String remark, UserDto salesperson) {
		this.seq = seq;
		this.customer = customer;
		this.startDate = startDate;
		this.status = status;
		this.remark = remark;
		this.setSalesperson(salesperson);
	}

	public ActivityDto() {
		this.startDate = new Date();
	}

	public UserDto getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(UserDto salesperson) {
		this.salesperson = salesperson;
	}
}
