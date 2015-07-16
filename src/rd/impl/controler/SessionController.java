package rd.impl.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import rd.spec.manager.SessionManager;

@Named
@SessionScoped
public class SessionController implements Serializable {
	private static final long serialVersionUID = 1L;


	@Inject SessionManager sessionManager;

	private List<SelectItem> currencyList;
	private List<SelectItem> links;
	private MenuModel model;
	private Map<Integer, Double> rates;

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
			return "../faces/manager.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("sale")) {
			return "../faces/salesperson.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("support")) {
			return "../faces/products.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("customer")) {
			return "../customers/customers.jsf";
		} else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("manager")) {
			return "../faces/manager.jsf";
		}
		return "";
	}

	public List<SelectItem> getLinks() {
		if (links == null || links.size() == 0) {
			links = new ArrayList<SelectItem>();
			links.add(new SelectItem("AutoCAD.jsf", "AutoCAD"));
			links.add(new SelectItem("AutoCAD.jsf", "quickdesk"));
			links.add(new SelectItem("AutoCAD.jsf", "Revit"));
			links.add(new SelectItem("AutoCAD.jsf", "Stormworks"));
		}
		System.out.println(links.size());
		return links;
	}

	public void setLinks(List<SelectItem> links) {
		this.links = links;
	}

	public MenuModel getModel() {
		if (model == null) {
			model = new DefaultMenuModel();
	        Submenu submenu = new Submenu();
	        submenu.setLabel("Dynamic Submenu 1");
	        MenuItem item = new MenuItem();
	        item.setValue("Dynamic Menuitem 1.1");
	        item.setUrl("../products/AutoCAD.jsf");
	        submenu.getChildren().add(item);
	        model.addSubmenu(submenu);

	        Submenu submenu2 = new Submenu();
	        submenu2.setLabel("Dynamic Submenu 2");
	        MenuItem item2 = new MenuItem();
	        item2.setValue("Dynamic Menuitem 1.1");
	        item2.setUrl("../products/AutoCAD.jsf");
	        submenu2.getChildren().add(item2);
	        model.addSubmenu(submenu2);
		}
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public void addMenuItem(String label, String url) {
		MenuItem item = new MenuItem();
        item.setValue(label);
        item.setUrl("../products/" + url);

		List<UIComponent> current = model.getContents();
		Submenu menu1 = (Submenu) current.get(0);

		menu1.getChildren().add(item);
		System.out.println("addmenuitem called");
	}

	public Map<Integer, Double> getRates() {
		if (rates == null) {
			rates = new HashMap<Integer, Double>();
			rates.put(1, 1.0);
			rates.put(2, 0.47);
			rates.put(3, 0.74);
		}
		return rates;
	}

	public void setRates(Map<Integer, Double> rates) {
		this.rates = rates;
	}

	public String logout() {
		sessionManager.logoff();
		return "../portal.jsf?faces-redirect=true";
	}
}
