package rd.impl.controler;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class Language implements Serializable {
	private static final long serialVersionUID = 1L;
	private String locale;

	private static Map<String, Object> countries;

	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("English", Locale.ENGLISH);
		countries.put("French", Locale.FRENCH);
	}

	public Map<String, Object> getCountries() {
		return countries;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	// value change event listener
	public void localeChanged(ValueChangeEvent e) {
		System.out.println("Language.localeChanged()");
		String newLocaleValue = e.getNewValue().toString();
		for (Map.Entry<String, Object> entry : countries.entrySet()) {
			if (entry.getValue().toString().equals(newLocaleValue)) {
				FacesContext.getCurrentInstance().getViewRoot()
						.setLocale((Locale) entry.getValue());
			}
		}
	}

	public void changeLocale() {
		for (Map.Entry<String, Object> entry : countries.entrySet()) {
			if (entry.getValue().toString().equals(locale)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
			}
		}
	}
}
