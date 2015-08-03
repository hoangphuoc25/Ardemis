package rd.impl.service;

import rd.spec.service.CategoryService;

public class CategoryServiceImpl implements CategoryService, Serializable {
private static final long serialVersionUID = 4822474486634242542L;
private Transaction transaction;
	public List<ProductDto> getProductByCategory(String cat) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> result = categoryDao.getProductByCategory(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void addCategory(String category) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			 categoryDao.addCategory(transaction);
			transaction.commit();
					} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void addProductCategory(ProductDto product) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			 categoryDao.addProductCategory(transaction);
			transaction.commit();
					} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public void deleteProductCategory(ProductDto prod) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			 categoryDao.deleteProductCategory(transaction);
			transaction.commit();
					} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CategoryDto> getCategoryByProduct(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CategoryDto> result = categoryDao.getCategoryByProduct(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<CategoryDto> getAll() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<CategoryDto> result = categoryDao.getAll(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
