package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.ProductDto;
import rd.dto.RelatedProductDto;
import rd.spec.dao.RelatedProductDao;
import rd.spec.dao.Transaction;
import rd.spec.service.RelatedProductService;

public class RelatedProductServiceImpl implements RelatedProductService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private RelatedProductDao relatedProductDao;

	@Inject
	public RelatedProductServiceImpl(Transaction transaction, RelatedProductDao relatedProductDao) {
		this.transaction = transaction;
		this.relatedProductDao = relatedProductDao;
	}

	public List<ProductDto> getRelatedProduct(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			List<ProductDto> result = relatedProductDao.getRelatedProduct(
					transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void addRelatedProducts(RelatedProductDto rpd) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			relatedProductDao.addRelatedProducts(transaction, rpd);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
