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

	public FeedbackDto(int seq, ProductDto product, int rating,
			UserDto customer, String feedback) {
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

	private int feature;

	public int getFeature() {
		return this.feature;
	}

	public void setFeature(int feature) {
		this.feature = feature;
	}

	private int thirdPartySupport;

	public int getThirdPartySupport() {
		return this.thirdPartySupport;
	}

	public void setThirdPartySupport(int thirdPartySupport) {
		this.thirdPartySupport = thirdPartySupport;
	}

	private int performance;

	public int getPerformance() {
		return this.performance;
	}

	public void setPerformance(int performance) {
		this.performance = performance;
	}

	private int userExperience;

	public int getUserExperience() {
		return this.userExperience;
	}

	public void setUserExperience(int userExperience) {
		this.userExperience = userExperience;
	}

	private int userInterface;

	public int getUserInterface() {
		return this.userInterface;
	}

	public void setUserInterface(int userInterface) {
		this.userInterface = userInterface;
	}

	private int usability;

	public int getUsability() {
		return this.usability;
	}

	public void setUsability(int usability) {
		this.usability = usability;
	}

	private int stability;

	public int getStability() {
		return this.stability;
	}

	public void setStability(int stability) {
		this.stability = stability;
	}

	public FeedbackDto(int feature, int thirdPartySupport, int performance,
			int userExperience, int userInterface, int usability, int stability) {
		this.feature = feature;
		this.thirdPartySupport = thirdPartySupport;
		this.performance = performance;
		this.userExperience = userExperience;
		this.userInterface = userInterface;
		this.usability = usability;
		this.stability = stability;
	}

	public FeedbackDto(int seq, ProductDto product, int rating,
			UserDto customer, String feedback, int feature,
			int thirdPartySupport, int performance, int userExperience,
			int userInterface, int usability, int stability) {
		this.seq = seq;
		this.product = product;
		this.rating = rating;
		this.customer = customer;
		this.feedback = feedback;
		this.feature = feature;
		this.thirdPartySupport = thirdPartySupport;
		this.performance = performance;
		this.userExperience = userExperience;
		this.userInterface = userInterface;
		this.usability = usability;
		this.stability = stability;
	}

	public FeedbackDto() {}
}
