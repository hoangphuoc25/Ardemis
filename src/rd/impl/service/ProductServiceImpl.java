package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.CategoryDto;
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
	public List<ProductDto> searchByName(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> search = productDao.searchByName(transaction, name);
			transaction.commit();
			return search;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ProductDto> getAll() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> all = productDao.getAll(transaction);
			transaction.commit();
			return all;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public int getSeq() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			int seq = productDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public ProductDto getByName(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			ProductDto result = productDao.getByName(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CategoryDto> getCategoryByProduct(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CategoryDto> result = productDao.getCategoryByProduct(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ProductDto> searchByCategories(List<String> categories) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> result = productDao.searchByCategories(transaction, categories);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ProductDto> searchByProductDesc(String keyword) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> result = productDao.searchByProductDesc(transaction, keyword);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
