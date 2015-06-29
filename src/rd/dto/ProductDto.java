package rd.dto;

public class ProductDto {
	private int seq;
	private String name;
	private String summary;
	private String target;
	private double price;

	public ProductDto(int seq, String name, String summary, String target, double price2) {
		this.seq = seq;
		this.name = name;
		this.summary = summary;
		this.target = target;
		this.price = price2;
	}

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
