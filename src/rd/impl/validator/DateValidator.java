package rd.impl.validator;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class DateValidator implements Validator {
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		Date d = (Date) value;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int year = c.get(Calendar.YEAR);
		Calendar e = Calendar.getInstance();
		e.setTime(new Date());
		if (year < 0 || year > e.get(Calendar.YEAR)) {
			FacesMessage msg = new FacesMessage("Invalid date", null);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
