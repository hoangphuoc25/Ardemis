package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ProductDto;
import rd.dto.PromotionDto;

public interface PromotionDao {
	public void addPromotion(Transaction transaction, PromotionDto promo) throws IOException;
	public PromotionDto getById(Transaction transaction, int seq) throws IOException;
	public List<PromotionDto> getByProduct(Transaction transaction, int seq) throws IOException;
	public void deletePromotion(Transaction transaction, int seq) throws IOException;
	public void updatePromotion(Transaction transaction, PromotionDto promo) throws IOException;
	public List<PromotionDto> getAll(Transaction transaction) throws IOException;
	public List<PromotionDto> getActive(Transaction transaction) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<ProductDto> getProductList(Transaction transaction, int seq) throws IOException;
	public List<PromotionDto> getActivePromotionByProduct(Transaction transaction, int seq) throws IOException;
}
