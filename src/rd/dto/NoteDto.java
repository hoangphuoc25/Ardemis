package rd.dto;

import java.util.Date;

public class NoteDto {
	private UserDto fromUser;
	private UserDto toUser;
	private int seq;
	private String note;
	private Date createdDate;
	private String status;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public UserDto getFromUser() {
		return this.fromUser;
	}

	public void setFromUser(UserDto fromUser) {
		this.fromUser = fromUser;
	}

	public UserDto getToUser() {
		return this.toUser;
	}

	public void setToUser(UserDto toUser) {
		this.toUser = toUser;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public NoteDto(int seq, UserDto from, UserDto to, String note, Date createdDate) {
		this.seq = seq;
		this.fromUser = from;
		this.toUser = to;
		this.note = note;
		this.createdDate = createdDate;
		this.status = "UNREAD";
	}

	public NoteDto(int seq, UserDto from, UserDto to, String note, Date createdDate, String status) {
		this.seq = seq;
		this.fromUser = from;
		this.toUser = to;
		this.note = note;
		this.createdDate = createdDate;
		this.status = status;
	}

	public NoteDto() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
