package rd.dto;

import java.util.Date;
import java.util.List;

public class ActivityDto {
	private int seq;
	private ContactDto contact;
	private Date startDate;
	private String status;
	private String remark;
	private UserDto salesperson;
	private CompanyDto customer;
	private List<ProductDto> products;
	private Date closingDate;
	private boolean selected;

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

	public ActivityDto(int seq, ContactDto contact, Date startDate, String status, String remark, UserDto salesperson, List<ProductDto> products) {
		this.seq = seq;
		this.contact = contact;
		this.startDate = startDate;
		this.status = status;
		this.remark = remark;
		this.setSalesperson(salesperson);
		this.products = products;
	}

	public ActivityDto(ActivityDto act) {
		this.seq = act.getSeq();
		this.contact = act.getContact();
		this.startDate = act.getStartDate();
		this.status = act.getStatus();
		this.remark = act.getRemark();
		this.salesperson = act.getSalesperson();
		this.products = act.getProducts();
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

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}

	public List<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDto> products) {
		this.products = products;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
}
