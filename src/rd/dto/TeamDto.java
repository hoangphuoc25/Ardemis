package rd.dto;

public class TeamDto {
	private int seq;
	private String name;
	private String remark;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public TeamDto(int seq, String name, String remark) {
		this.seq = seq;
		this.name = name;
		this.remark = remark;
	}

}
