package rd.dto;

import java.util.List;

public class RelatedProductDto {
	private ProductDto product;
	private List<ProductDto> related;

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

	public List<ProductDto> getRelated() {
		return related;
	}

	public void setRelated(List<ProductDto> related) {
		this.related = related;
	}

	public RelatedProductDto(ProductDto product, List<ProductDto> related) {
		this.product = product;
		this.related = related;
	}

	public RelatedProductDto() {
	}
}
