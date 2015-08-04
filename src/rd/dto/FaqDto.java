package rd.dto;

public class FaqDto {
	private int seq;
	private ProductDto product;
	private String question;
	private String answer;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public ProductDto getProduct() {
		return product;
	}

	public void setProduct(ProductDto product) {
		this.product = product;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public FaqDto(int seq, ProductDto product, String question, String answer) {
		this.seq = seq;
		this.product = product;
		this.question = question;
		this.answer = answer;
	}

	public FaqDto() {
	}
}
