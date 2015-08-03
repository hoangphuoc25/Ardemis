package rd.spec.dataCache;

import rd.dto.ProductDto;

public interface ProductCache {
	public ProductDto getProduct(int seq);
	public void putProduct(ProductDto product);
	public void clear();
	public void removeProduct(int seq);
}
