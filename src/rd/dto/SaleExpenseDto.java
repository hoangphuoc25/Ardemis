package rd.dto;

import java.util.Date;

public class SaleExpenseDto {
	private int seq;
	private UserDto salesperson;
	private Date receiptDate;
	private String purpose;
	private String receiptNo;
	private double amount;

	public SaleExpenseDto(int seq, UserDto salesPerson, Date receiptDate, String purpose, String receiptNo, double amount) {
		this.seq = seq;
		this.salesperson = salesPerson;
		this.receiptDate = receiptDate;
		this.purpose = purpose;
		this.receiptNo = receiptNo;
		this.amount = amount;
	}

	public SaleExpenseDto() {}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public UserDto getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(UserDto salesperson) {
		this.salesperson = salesperson;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
