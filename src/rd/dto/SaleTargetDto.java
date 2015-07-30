package rd.dto;

import java.util.Date;

public class SaleTargetDto {
	private int target;
	private Date fromDate;
	private Date toDate;
	private UserDto sale;
	private int current;

	public SaleTargetDto(UserDto sale, int target, Date fromDate, Date toDate,
			int current) {
		this.target = target;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.sale = sale;
		this.current = current;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public SaleTargetDto() {
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public UserDto getSale() {
		return sale;
	}

	public void setSale(UserDto sale) {
		this.sale = sale;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
}
