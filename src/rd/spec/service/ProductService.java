package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ProductDto;

public interface ProductService {
	public void addProduct(ProductDto product) throws IOException;
	public void removeProduct(int seq) throws IOException;
	public ProductDto getProductById(int seq) throws IOException;
	public void updateProduct(ProductDto product) throws IOException;
	public List<ProductDto> searchByName(String name) throws IOException;
}
