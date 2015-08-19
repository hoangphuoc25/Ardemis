package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ProductDto;
import rd.dto.RelatedProductDto;

public interface RelatedProductService {
	public List<ProductDto> getRelatedProduct(int seq) throws IOException;

	public void addRelatedProducts(RelatedProductDto rpd) throws IOException;
}
