package rd.dto;

import java.util.Date;
import java.util.List;

public class InvoiceDto {
	private int seq;
	private CompanyDto customer;
	private Date purchaseDate;
	private double amount;
	private List<ProductDto> products;

	public InvoiceDto(int seq, CompanyDto company, Date purchaseDate, double amount, List<ProductDto> products) {
		this.seq = seq;
		this.customer = company;
		this.purchaseDate = purchaseDate;
		this.amount = amount;
		this.products = products;
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
}
