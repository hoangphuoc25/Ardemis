package rd.impl.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named
@SessionScoped
public class SessionController implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<SelectItem> currencyList;

	public List<SelectItem> getCurrencyList() {
		if (currencyList == null) {
			currencyList = new ArrayList<SelectItem>();
			currencyList.add(new SelectItem(1, "SGD"));
			currencyList.add(new SelectItem(2, "GBP"));
			currencyList.add(new SelectItem(3, "USD"));
		}
		return currencyList;
	}

	public void setCurrencyList(List<SelectItem> currencyList) {
		this.currencyList = currencyList;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
		System.out.println("currency set:  " + currency);
	}

	private int currency;

	public String dashboardLink() {
		if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("admin")) {
			return "./manager.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("sale")) {
			return "./salesperson.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("support")) {
			return "./support.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("customer")) {
			return "../customers/customers.jsf";
		}
		return "";
	}
}
