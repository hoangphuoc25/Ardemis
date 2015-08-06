package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.CategoryDto;
import rd.dto.ProductDto;

public interface ProductService {
	public void addProduct(ProductDto product) throws IOException;
	public void removeProduct(int seq) throws IOException;
	public ProductDto getProductById(int seq) throws IOException;
	public void updateProduct(ProductDto product) throws IOException;
	public List<ProductDto> searchByName(String name) throws IOException;
	public List<ProductDto> getAll() throws IOException;
	public int getSeq() throws IOException;
	public ProductDto getByName(String name) throws IOException;
	public List<CategoryDto> getCategoryByProduct(int seq) throws IOException;
	public List<ProductDto> searchByCategories(List<String> categories) throws IOException;
	public List<ProductDto> searchByProductDesc(String keyword) throws IOException;
	public List<ProductDto> searchByPrice(int budget) throws IOException;
}
