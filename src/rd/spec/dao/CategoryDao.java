package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.CategoryDto;
import rd.dto.ProductDto;

public interface CategoryDao {
	public List<ProductDto> getProductByCategory(Transaction transaction, String cat) throws IOException;
	public void addCategory(Transaction transaction, String category) throws IOException;
	public void addProductCategory(Transaction transaction, ProductDto product) throws IOException;
	public void deleteProductCategory(Transaction transaction, ProductDto prod) throws IOException;
	public List<CategoryDto> getCategoryByProduct(Transaction transaction, int seq) throws IOException;
	public List<CategoryDto> getAll(Transaction transaction) throws IOException;
	public List<CategoryDto> searchCategory(Transaction transaction, String name) throws IOException;
}
