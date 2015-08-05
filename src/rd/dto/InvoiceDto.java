package rd.dto;

import java.util.Date;
import java.util.List;

public class InvoiceDto {
	private int seq;
	private CompanyDto customer;
	private Date purchaseDate;
	private double amount;
	private List<ProductDto> products;
	private UserDto salesperson;
	private ContactDto contact;

	public InvoiceDto(int seq, ContactDto contact, Date purchaseDate, double amount, List<ProductDto> products) {
		this.seq = seq;
		this.contact = contact;
		this.purchaseDate = purchaseDate;
		this.amount = amount;
		this.products = products;
	}

	public InvoiceDto(int seq, ContactDto contact, Date purchaseDate, double amount, List<ProductDto> products, UserDto salesperson) {
		this.seq = seq;
		this.contact = contact;
		this.purchaseDate = purchaseDate;
		this.amount = amount;
		this.products = products;
		this.salesperson = salesperson;
	}

	public InvoiceDto() {
		customer = new CompanyDto();
		this.purchaseDate = new Date();
	}

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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public List<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDto> products) {
		this.products = products;
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
}
