package rd.impl.validator;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import rd.dto.ProductDto;
import rd.spec.service.ProductService;

@FacesValidator(value="productValidator")
public class ProductValidator implements Validator {

	@Inject private ProductService productService;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		ProductDto prod = (ProductDto) value;
		List<ProductDto> search;
		try {
			search = productService.searchByName(prod.getName());
			if (search.size() > 0) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A product with this name already existed.", null);
				throw new ValidatorException(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
