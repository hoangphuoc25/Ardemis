package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.CategoryDto;
import rd.dto.ProductDto;
import rd.spec.dao.CategoryDao;
import rd.spec.dao.Transaction;
import rd.spec.service.CategoryService;

public class CategoryServiceImpl implements CategoryService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private CategoryDao categoryDao;

	@Inject
	public CategoryServiceImpl(Transaction transaction, CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
		this.transaction = transaction;
	}

	public List<ProductDto> getProductByCategory(String cat) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<ProductDto> result = categoryDao
					.getProductByCategory(transaction, cat);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addCategory(String category) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			categoryDao.addCategory(transaction, category);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addProductCategory(ProductDto product) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			categoryDao.addProductCategory(transaction, product);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteProductCategory(ProductDto prod) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			categoryDao.deleteProductCategory(transaction, prod);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<CategoryDto> getCategoryByProduct(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<CategoryDto> result = categoryDao
					.getCategoryByProduct(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public List<CategoryDto> getAll() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<CategoryDto> result = categoryDao.getAll(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CategoryDto> searchCategory(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CategoryDto> result = categoryDao.searchCategory(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
