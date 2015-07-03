package rd.dto;

public class CompanyDto {
	private int seq;
	private String name;
	private String size;
	private String industry;
	private String type;
	private int yearFounded;
	private String location;
	private String remark;
	private String phone;

	public CompanyDto(int seq, String name, String size, String industry, String type,
			int yearFounded, String location, String phone, String remark) {
		this.name = name;
		this.seq = seq;
		this.size = size;
		this.industry = industry;
		this.type = type;
		this.yearFounded = yearFounded;
		this.location = location;
		this.remark = remark;
		this.phone = phone;
	}

	public CompanyDto() {
		super();
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getYearFounded() {
		return yearFounded;
	}

	public void setYearFounded(int yearFounded) {
		this.yearFounded = yearFounded;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
