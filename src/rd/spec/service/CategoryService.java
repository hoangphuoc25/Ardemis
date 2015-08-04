package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.CategoryDto;
import rd.dto.ProductDto;

public interface CategoryService {
	public List<ProductDto> getProductByCategory(String cat) throws IOException;
	public void addCategory(String category) throws IOException;
	public void addProductCategory(ProductDto product) throws IOException;
	public void deleteProductCategory(ProductDto prod) throws IOException;
	public List<CategoryDto> getCategoryByProduct(int seq) throws IOException;
	public List<CategoryDto> getAll() throws IOException;
	public List<CategoryDto> searchCategory(String name) throws IOException;
}
