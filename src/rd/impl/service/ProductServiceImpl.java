package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;

import rd.dto.ProductDto;
import rd.spec.dao.ProductDao;
import rd.spec.dao.Transaction;
import rd.spec.service.ProductService;

public class ProductServiceImpl implements ProductService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private ProductDao productDao;

	@Inject
	public ProductServiceImpl(Transaction transaction, ProductDao productDao) {
		this.transaction = transaction;
		this.productDao = productDao;
	}

	public void addProduct(ProductDto product) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			productDao.addProduct(transaction, product);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void removeProduct(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			productDao.removeProduct(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public ProductDto getProductById(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			ProductDto product = productDao.getProductById(transaction, seq);
			transaction.commit();
			return product;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void updateProduct(ProductDto product) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			productDao.updateProduct(transaction, product);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
