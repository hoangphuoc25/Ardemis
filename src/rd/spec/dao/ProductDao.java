package rd.spec.dao;

import java.io.IOException;

import rd.dto.ProductDto;

public interface ProductDao {
	public void addProduct (Transaction transaction, ProductDto product) throws IOException;
	public void removeProduct (Transaction transaction, int seq) throws IOException;
	public ProductDto getProductById (Transaction transaction, int seq) throws IOException;
	public void updateProduct (Transaction transaction, ProductDto product) throws IOException;
}
