package rd.dto;

public class WrProdDto {
	private ProductDto prod;
	private String licenseType;
	private int duration;

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public ProductDto getProd() {
		return prod;
	}

	public void setProd(ProductDto prod) {
		this.prod = prod;
	}

	public WrProdDto(ProductDto prod, String type, int duration) {
		this.prod = prod;
		this.licenseType = type;
		this.duration = duration;
	}

	public WrProdDto() {
	}
}
