package rd.dto;

import java.util.Date;

public class ContactDto {
	private int seq;
	private String name;
	private String company;
	private String phone;
	private String email;
	private String gender;
	private String language;
	private String address;
	private UserDto assignee;
	private String contactStatus;
	private Date createdDate;
	private Date lastDate;
	private String source;
	private String jobTitle;

	public ContactDto() {
		this.language = "English";
		this.gender = "Male";
		this.contactStatus = "New";
		this.source = "Personal contact";
	}

	public ContactDto(int seq, String name, String gender, String phone,
			String email, String company, String language, String address, UserDto assignee, String contactStatus, String source) {
		this.seq = seq;
		this.name = name;
		this.company = company;
		this.phone = phone;
		this.email = email;
		this.gender = gender;
		this.language = language;
		this.address = address;
		this.assignee = assignee;
		this.setContactStatus(contactStatus);
		this.source = source;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public UserDto getAssignee() {
		return assignee;
	}

	public void setAssignee(UserDto assignee) {
		this.assignee = assignee;
	}

	public String getContactStatus() {
		return contactStatus;
	}

	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
}
