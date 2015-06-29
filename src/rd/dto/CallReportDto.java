package rd.dto;

import java.util.Date;

public class CallReportDto {

	private int seq;
	private String phone;
	private Date callDate;
	private String note;
	private String rate;
	private ContactDto contact;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCallDate() {
		return callDate;
	}

	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public CallReportDto(int seq, ContactDto contact, String phone, Date callDate,
			String note, String rate) {
		this.seq = seq;
		this.phone = phone;
		this.callDate = callDate;
		this.note = note;
		this.rate = rate;
		this.contact = contact;
	}

	public CallReportDto() {}

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}
}
