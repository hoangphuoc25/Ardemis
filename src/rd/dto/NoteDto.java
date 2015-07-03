package rd.dto;

import java.util.Date;

public class NoteDto {
	private UserDto fromUser;
	private UserDto toUser;
	private int seq;
	private String note;

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

	private Date createdDate;

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
	}

	public NoteDto() {
		super();
	}
}
