package rd.dto;

public class ScriptDto {
	private int seq;
	private String description;
	private String detail;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public ScriptDto(int seq, String description, String detail) {
		this.seq = seq;
		this.description = description;
		this.detail = detail;
	}

	public ScriptDto() {
	}
}
