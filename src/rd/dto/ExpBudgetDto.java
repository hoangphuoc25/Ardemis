package rd.dto;

import java.util.Date;

public class ExpBudgetDto {
	private UserDto user;
	private int budget;
	private Date from;
	private Date to;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public ExpBudgetDto(UserDto user, int budget, Date from, Date to) {
		this.user = user;
		this.budget = budget;
		this.from = from;
		this.to = to;
	}

	public ExpBudgetDto() {
	}
}
