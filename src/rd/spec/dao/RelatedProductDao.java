package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ProductDto;
import rd.dto.RelatedProductDto;

public interface RelatedProductDao {
	public List<ProductDto> getRelatedProduct(Transaction transaction, int seq) throws IOException;
	public void addRelatedProducts(Transaction transaction, RelatedProductDto rpd) throws IOException;
}
