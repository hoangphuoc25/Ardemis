package rd.dto;

public class FeedbackDto {
	private ProductDto product;
	private int rating;
	private UserDto customer;

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public UserDto getCustomer() {
		return customer;
	}

	public void setCustomer(UserDto customer) {
		this.customer = customer;
	}

	public FeedbackDto(int seq, ProductDto product, int rating, UserDto customer,
			String feedback) {
		this.seq = seq;
		this.product = product;
		this.rating = rating;
		this.customer = customer;
		this.feedback = feedback;
	}

	private String feedback;

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	private int seq;

	public int getSeq() {
		return this.seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
}
