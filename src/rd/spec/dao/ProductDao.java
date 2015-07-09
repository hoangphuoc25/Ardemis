package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ProductDto;

public interface ProductDao {
	public void addProduct (Transaction transaction, ProductDto product) throws IOException;
	public void removeProduct (Transaction transaction, int seq) throws IOException;
	public ProductDto getProductById (Transaction transaction, int seq) throws IOException;
	public void updateProduct (Transaction transaction, ProductDto product) throws IOException;
	public List<ProductDto> searchByName(Transaction transaction, String name) throws IOException;
	public List<ProductDto> getAll(Transaction transaction) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public ProductDto getByName(Transaction transaction, String name) throws IOException;
}
