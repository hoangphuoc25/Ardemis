package rd.dto;

public class WrUserDto {
	private UserDto sale;
	private int assignedContacts;
	public int current;

	public int getAssignedContacts() {
		return assignedContacts;
	}

	public void setAssignedContacts(int assignedContacts) {
		this.assignedContacts = assignedContacts;
	}

	public UserDto getSale() {
		return sale;
	}

	public void setSale(UserDto sale) {
		this.sale = sale;
	}

	public WrUserDto(UserDto sale, int assignedContacts) {
		this.sale = sale;
		this.assignedContacts = assignedContacts;
		current = assignedContacts;
	}
}
