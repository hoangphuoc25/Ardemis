package rd.dto;

import java.util.Date;
import java.util.List;

public class PromotionDto {
	private int seq;
	private Date startDate;
	private Date endDate;
	private List<ProductDto> productList;
	private int discount;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ProductDto> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDto> productList) {
		this.productList = productList;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public PromotionDto(int seq, Date startDate, Date endDate, List<ProductDto> productList, int discount) {
		this.seq = seq;
		this.startDate = startDate;
		this.endDate = endDate;
		this.productList = productList;
		this.discount = discount;
	}

	public PromotionDto() {
	}
}
