package rd.dto;

public class CategoryDto {
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public CategoryDto(String category) {
		this.category = category;
	}

	public CategoryDto() {
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	private boolean selected;
}
