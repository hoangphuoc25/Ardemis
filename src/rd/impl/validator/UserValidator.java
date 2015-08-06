package rd.impl.validator;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rd.dto.UserDto;
import rd.spec.service.UserService;

public class UserValidator implements Validator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private UserService userService;

	public UserValidator() {
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		String id = value.toString();
		logger.error("ID:" + id);
		if (id == null || id.isEmpty()) {
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "ID is required", null));
		}
		try {
			if (userService == null) {
				logger.error("USERSERVICE IS NULL");
			}
			UserDto user = userService.findUserById(id);
			if (user != null) {
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "UserID existed", null));
			}
		} catch (IOException e) {
			logger.error("ERROR");
			e.printStackTrace();
		}
	}
}
