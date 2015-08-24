package rd.dto;

public class WrStatisticsDto {
	private UserDto salesperson;
	private double total;
	private int deals;
	private int calls;
	private TeamDto team;
	private double revenuePercent;

	public UserDto getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(UserDto salesperson) {
		this.salesperson = salesperson;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getDeals() {
		return deals;
	}

	public void setDeals(int deals) {
		this.deals = deals;
	}

	public int getCalls() {
		return calls;
	}

	public void setCalls(int calls) {
		this.calls = calls;
	}

	public TeamDto getTeam() {
		return team;
	}

	public void setTeam(TeamDto team) {
		this.team = team;
	}

	public WrStatisticsDto(UserDto user, double total, int deals, int calls, TeamDto team, double percent) {
		this.salesperson = user;
		this.total = total;
		this.deals = deals;
		this.calls = calls;
		this.team = team;
		this.revenuePercent = percent;
	}

	public double getRevenuePercent() {
		return revenuePercent;
	}

	public void setRevenuePercent(double revenuePercent) {
		this.revenuePercent = revenuePercent;
	}
}
