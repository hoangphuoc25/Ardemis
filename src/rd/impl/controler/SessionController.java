package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

import rd.spec.manager.SessionManager;
import rd.spec.service.UserService;

@Named
@SessionScoped
public class SessionController implements Serializable {
	private static final long serialVersionUID = 1L;


	@Inject SessionManager sessionManager;
	@Inject UserService userService;

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
		if (currency == 0) {
			currency = 1;
		}
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
	        DefaultSubMenu DefaultSubMenu = new DefaultSubMenu();
	        DefaultSubMenu.setLabel("Dynamic DefaultSubMenu 1");
	        DefaultMenuItem item = new DefaultMenuItem();
	        item.setValue("Dynamic DefaultMenuItem 1.1");
	        item.setUrl("../products/AutoCAD.jsf");
	        DefaultSubMenu.addElement(item);
	        model.addElement(DefaultSubMenu);

	        DefaultSubMenu DefaultSubMenu2 = new DefaultSubMenu();
	        DefaultSubMenu2.setLabel("Dynamic DefaultSubMenu 2");
	        DefaultMenuItem item2 = new DefaultMenuItem();
	        item2.setValue("Dynamic DefaultMenuItem 1.1");
	        item2.setUrl("../products/AutoCAD.jsf");
	        DefaultSubMenu2.addElement(item2);
	        model.addElement(DefaultSubMenu2);
		}
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public void addDefaultMenuItem(String label, String url) {
		DefaultMenuItem item = new DefaultMenuItem();
        item.setValue(label);
        item.setUrl("../products/" + url);

		List<MenuElement> current = model.getElements();
		DefaultSubMenu menu1 = (DefaultSubMenu) current.get(0);

		menu1.addElement(item);
		System.out.println("addDefaultMenuItem called");
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

	// @PostConstruct -- only works with pages with no conversation started at the beginning (in preRenderView)
	public void init() throws IOException {
		System.out.println("SessionController.init()");
		System.out.println("initing");
		userService.updateLoginTime(sessionManager.getLoginUser().getId(), new Date());
	}

	public String getDashboardLink() throws IOException {
		if (dashboardLink == null || dashboardLink.isEmpty()) {
			userService.updateLoginTime(sessionManager.getLoginUser().getId(), new Date());
			dashboardLink = dashboardLink();
		}
		return dashboardLink;
	}

	public void setDashboardLink(String dashboardLink) {
		this.dashboardLink = dashboardLink;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	private String dashboardLink;
	private Date lastLoginTime;
}
