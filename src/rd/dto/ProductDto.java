package rd.dto;

import java.util.List;

public class ProductDto {
	private int seq;
	private String name;
	private String summary;
	private String target;
	private double price;
	private double permanentPrice;
	private boolean selected = false;
	private List<CategoryDto> category;

	private String type;
	private int duration;
	private int quantity;
	private int storage;

	public ProductDto(int seq, String name, String summary, String target, double price2) {
		this.seq = seq;
		this.name = name;
		this.summary = summary;
		this.target = target;
		this.price = price2;
		this.quantity = 1;
		this.storage = 1;
	}

	public ProductDto(int seq, String name, String summary, String target, double price2, List<CategoryDto> category, double permanentPrice) {
		this.seq = seq;
		this.name = name;
		this.summary = summary;
		this.target = target;
		this.price = price2;
		this.category = category;
		this.permanentPrice = permanentPrice;
		this.quantity = 1;
		this.storage = 1;
	}

	public ProductDto() {
		this.quantity = 1;
	}

	public ProductDto(ProductDto selectedProd) {
		this.seq = selectedProd.seq;
		this.name = selectedProd.name;
		this.summary = selectedProd.summary;
		this.target = selectedProd.target;
		this.price = selectedProd.price;
		this.category = selectedProd.category;
		this.permanentPrice = selectedProd.getPermanentPrice();
		this.quantity = 1;
		this.storage = 1;
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

	public List<CategoryDto> getCategory() {
		return category;
	}

	public void setCategory(List<CategoryDto> category) {
		this.category = category;
	}

	public double getPermanentPrice() {
		return permanentPrice;
	}

	public void setPermanentPrice(double permanentPrice) {
		this.permanentPrice = permanentPrice;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}
}
