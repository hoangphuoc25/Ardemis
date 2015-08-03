package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.ProductDto;
import rd.dto.PromotionDto;
import rd.spec.dao.PromotionDao;
import rd.spec.dao.Transaction;
import rd.spec.service.PromotionService;

public class PromotionServiceImpl implements PromotionService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private PromotionDao promoDao;

	@Inject
	public PromotionServiceImpl(Transaction transaction, PromotionDao promoDao) {
		this.transaction = transaction;
		this.promoDao = promoDao;
	}

	@Override
	public void addPromotion(PromotionDto promo) throws IOException {
		try {
			transaction.begin();
			promoDao.addPromotion(transaction, promo);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public PromotionDto getById(int seq) throws IOException {
		try {
			transaction.begin();
			PromotionDto result = promoDao.getById(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public List<PromotionDto> getByProduct(int seq) throws IOException {
		try {
			transaction.begin();
			List<PromotionDto> result = promoDao.getByProduct(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void deletePromotion(int seq) throws IOException {
		try {
			transaction.begin();
			promoDao.deletePromotion(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void updatePromotion(PromotionDto promo) throws IOException {
		// TODO Auto-generated method stub
		try {
			transaction.begin();
			promoDao.updatePromotion(transaction, promo);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public List<PromotionDto> getAll() throws IOException {
		// TODO Auto-generated method stub
		try {
			transaction.begin();
			List<PromotionDto> result = promoDao.getAll(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public List<PromotionDto> getActive() throws IOException {
		try {
			transaction.begin();
			List<PromotionDto> result = promoDao.getActive(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ProductDto> getProductList(int seq) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ProductDto> result = promoDao.getProductList(transaction, seq);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public int getSeq() throws IOException {
		try{
			transaction.begin();
			int seq = promoDao.getSeq(transaction);
			transaction.commit();
			return seq;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
