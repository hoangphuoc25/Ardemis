package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ProductDto;
import rd.dto.PromotionDto;

public interface PromotionService {
	public void addPromotion(PromotionDto promo) throws IOException;
	public PromotionDto getById(int seq) throws IOException;
	public List<PromotionDto> getByProduct(int seq) throws IOException;
	public void deletePromotion(int seq) throws IOException;
	public void updatePromotion(PromotionDto promo) throws IOException;
	public List<PromotionDto> getAll() throws IOException;
	public List<PromotionDto> getActive() throws IOException;
	public int getSeq() throws IOException;
	public List<ProductDto> getProductList(int seq) throws IOException;
	public List<PromotionDto> getActivePromotionByProduct(int seq) throws IOException;
	public List<PromotionDto> searchPromotion(String partial) throws IOException;
}
