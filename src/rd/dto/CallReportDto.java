package rd.dto;

import java.util.Date;

public class CallReportDto {
	private int seq;
	private CompanyDto customer;

	public CompanyDto getCustomer() {
		return this.customer;
	}

	public void setCustomer(CompanyDto customer) {
		this.customer = customer;
	}

	private Date callTime;

	public Date getCallTime() {
		return this.callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	private String detail;

	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public CallReportDto(int seq, CompanyDto customer, Date callTime, String detail, String rating, UserDto user, ProductDto product) {
		this.seq = seq;
		this.customer = customer;
		this.callTime = callTime;
		this.detail = detail;
		this.rating = rating;
		this.salesperson = user;
		this.product = product;
	}

	public CallReportDto() {}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	private String rating;
	public String getRating() {
		return this.rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}

	public UserDto getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(UserDto salesperson) {
		this.salesperson = salesperson;
	}

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

	private UserDto salesperson;
	private ProductDto product;
}
