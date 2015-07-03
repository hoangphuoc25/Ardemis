package rd.dto;

public class SupportTicketDto {
	private int seq;
	private String name;
	private CompanyDto company;
	private int categoryId;
	private String phone;
	private String email;
	private String detail;
	private String status;
	private String priority;

	// TODO: add priority to database

	public SupportTicketDto(int seq, String name, CompanyDto company, int categoryId, String phone, String email, String detail, String status, String priority) {
		this.seq = seq;
		this.name = name;
		this.company = company;
		this.categoryId = categoryId;
		this.phone = phone;
		this.email = email;
		this.detail = detail;
		this.status = status;
		this.priority = priority;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompanyDto getCompany() {
		return company;
	}

	public void setCompany(CompanyDto company) {
		this.company = company;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
}
