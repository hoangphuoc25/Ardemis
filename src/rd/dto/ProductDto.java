package rd.dto;

public class ProductDto {
	private int seq;
	private String name;
	private String summary;
	private String target;
	private double price;
	private boolean selected = false;

	public ProductDto(int seq, String name, String summary, String target, double price2) {
		this.seq = seq;
		this.name = name;
		this.summary = summary;
		this.target = target;
		this.price = price2;
	}

	public ProductDto() {
	}

	public ProductDto(ProductDto selectedProd) {
		this.seq = selectedProd.seq;
		this.name = selectedProd.name;
		this.summary = selectedProd.summary;
		this.target = selectedProd.target;
		this.price = selectedProd.price;
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
