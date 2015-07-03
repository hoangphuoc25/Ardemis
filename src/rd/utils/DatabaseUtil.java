package rd.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.RoleDto;
import rd.dto.TeamDto;
import rd.spec.service.RoleService;
import rd.spec.service.TeamService;

@Named
@Singleton
public class DatabaseUtil {

	private List<RoleDto> roleList;
	private Map<Integer, String> categoryList;
	private Map<String, Double> currencyRate;
	private List<String> currency;
	private Map<String, String> language;
	private List<String> ticketStatus;
	private Map<Integer, TeamDto> teamList;

	@Inject private RoleService roleService;
	@Inject private TeamService teamService;

	@PostConstruct
	public void init() throws IOException {
		setRoleList(roleService.getAllList());
		initiateCategoryList();
		initiateCurrencyRate();
		initiateCurrency();
		initiateLanguage();
		initiateTicketStatus();
		initiateTeamList();
	}

	private void initiateTeamList() throws IOException {
		teamList = new HashMap<Integer, TeamDto>();
		List<TeamDto> teams = teamService.getAll();
		for (TeamDto team: teams) {
			teamList.put(team.getSeq(), team);
		}
	}

	private void initiateTicketStatus() {
		ticketStatus = new ArrayList<String>(Arrays.asList("New", "Pending", "Work In Progress", "Resolved", "Redirected"));
	}

	private void initiateLanguage() {
		language = new HashMap<String, String>();
		language.put("EN", "English");
		language.put("JP", "Japanese");
		language.put("CN", "Chinese");
	}

	private void initiateCurrency() {
		currency = new ArrayList<String>(Arrays.asList("USD", "SGD", "GBP", "JPY", "AUD", "EUR"));
	}

	private void initiateCurrencyRate() {
		currencyRate = new HashMap<String, Double>();
		currencyRate.put("USD", 1.344);
		currencyRate.put("SGD", 1.0);
		currencyRate.put("GBP", 2.116);
		currencyRate.put("JPY", 0.019);
		currencyRate.put("AUD", 1.039);
		currencyRate.put("EUR", 1.504);
	}

	private void initiateCategoryList() {
		this.categoryList = new HashMap<Integer, String>();
		categoryList.put(1, "Crash");
		categoryList.put(2, "Bug");
		categoryList.put(3, "Not working as intended");
		categoryList.put(4, "Need clarifications");
		categoryList.put(5, "Support request");
	}

	public List<RoleDto> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
	}

	public Map<Integer, String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(Map<Integer, String> categoryList) {
		this.categoryList = categoryList;
	}

	public Map<String, Double> getCurrencyRate() {
		return currencyRate;
	}

	public void setCurrencyRate(Map<String, Double> currencyRate) {
		this.currencyRate = currencyRate;
	}

	public List<String> getCurrency() {
		return currency;
	}

	public void setCurrency(List<String> currency) {
		this.currency = currency;
	}

	public Map<String, String> getLanguage() {
		return language;
	}

	public void setLanguage(Map<String, String> language) {
		this.language = language;
	}

	public List<String> getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(List<String> ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public Map<Integer, TeamDto> getTeamList() {
		return teamList;
	}

	public void setTeamList(Map<Integer, TeamDto> teamList) {
		this.teamList = teamList;
	}
}
