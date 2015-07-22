package rd.impl.controler;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class LanguageBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Locale locale;

	public Locale getLocale() {
		if (locale == null) {
			locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public void changeLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
		System.out.println(language);
		this.localeCode = language;
	}

	private String localeCode;

	private static Map<String, Object> countries;
	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("Select", Locale.ENGLISH); // label, value
		countries.put("English", Locale.ENGLISH); // label, value
		countries.put("French", Locale.FRENCH);
	}

	public Map<String, Object> getCountriesInMap() {
		return countries;
	}

	public String getLocaleCode() {
		if (localeCode == null || localeCode.isEmpty()) {
			localeCode = getLocale().getLanguage();
		}
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	// value change event listener
	public void countryLocaleCodeChanged(ValueChangeEvent e) {
		String newLocaleValue = e.getNewValue().toString();
		System.out.println("LanguageBean.countryLocaleCodeChanged()");
		for (Map.Entry<String, Object> entry : countries.entrySet()) {
			if (entry.getValue().toString().equals(newLocaleValue)) {
				System.out.println(entry.getValue().toString());
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
			}
		}
	}

	public void countryLocaleCodeChanged(AjaxBehaviorEvent event) {
		String locale = (String) ((UIOutput)event.getSource()).getValue();
		System.out.println("LanguageBean.countryLocaleCodeChanged()");
		System.out.println(locale);
		for (Map.Entry<String, Object> entry : countries.entrySet()) {
			if (entry.getValue().toString().equals(locale)) {
				System.out.println(entry.getValue().toString());
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
			}
		}
	}

	@PostConstruct
	public void init() {
		locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		System.out.println(locale.getLanguage());
	}
}
