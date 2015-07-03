package rd.impl.validator;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.CompanyDto;
import rd.spec.service.CompanyService;
@Named
public class CompanyValidator implements Validator {

	@Inject CompanyService companyService;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String name = value.toString();
		try {
			List<CompanyDto> comp = companyService.searchCompanyByName(name);
			if (comp != null && comp.size() != 0) {
				throw new ValidatorException(new FacesMessage("A company with this name alreay existed", null));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
