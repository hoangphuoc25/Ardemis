package rd.dto;

public class ContactDto {
	private int seq;
	private String name;
	private CompanyDto company;
	private String phone;
	private String email;
	private String gender;
	private String language;

	public ContactDto() {
		this.language = "English";
		this.gender = "Male";
	}

	public ContactDto(int seq, String name, String gender, String phone,
			String email, CompanyDto company, String language) {
		this.seq = seq;
		this.name = name;
		this.company = company;
		this.phone = phone;
		this.email = email;
		this.gender = gender;
		this.language = language;
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
}
