package rd.dto;

import java.util.Date;

public class CallReportDto {
	private int seq;
	private ContactDto contact;
	private UserDto salesperson;
	private ProductDto product;
	private int callBack;
	private String rating;
	private Date callTime;
	private String detail;

	public Date getCallTime() {
		return this.callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public CallReportDto(int seq, ContactDto contact, Date callTime,
			String detail, String rating, UserDto user, int callBack) {
		this.seq = seq;
		this.contact = contact;
		this.callTime = callTime;
		this.detail = detail;
		this.rating = rating;
		this.salesperson = user;
		this.callBack = callBack;
	}

	public CallReportDto() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

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

	public int getCallBack() {
		return callBack;
	}

	public void setCallBack(int callBack) {
		this.callBack = callBack;
	}

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}
}
