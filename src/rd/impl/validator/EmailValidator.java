package rd.impl.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

	private static final String PATTERN = "^[_A-Za-z0-9-]+(\\."
			+ "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*"
			+ "(\\.[A-Za-z]{2,})$";
	private Pattern pattern;
	private Matcher matcher;

	public EmailValidator() {
		pattern = Pattern.compile(PATTERN);
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent component,
			Object value) throws ValidatorException {
		matcher = pattern.matcher(value.toString());
		if (!matcher.matches()) {
			FacesMessage facesMsg = new FacesMessage("Email malformed",
					"Invalid email format");
			facesMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMsg);
		}
	}


}
