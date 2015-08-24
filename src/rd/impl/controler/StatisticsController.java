package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import rd.dto.InvoiceDto;
import rd.dto.NoteDto;
import rd.dto.ProductDto;
import rd.dto.SaleTargetDto;
import rd.dto.TeamDto;
import rd.dto.UserDto;
import rd.dto.WrStatisticsDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.CallReportService;
import rd.spec.service.InvoiceService;
import rd.spec.service.NoteService;
import rd.spec.service.SaleTargetService;
import rd.spec.service.TeamService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class StatisticsController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject TeamService teamService;
	@Inject UserService userService;

	public void reload() {
		conversationBegin();
	}

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if(!conversation.isTransient()){
			conversation.end();
		}
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public HorizontalBarChartModel getEmployeeModel() {
		if (employeeModel == null) {
			employeeModel = new HorizontalBarChartModel();
		}
		return employeeModel;
	}

	public void setEmployeeModel(HorizontalBarChartModel employeeModel) {
		this.employeeModel = employeeModel;
	}

	public HorizontalBarChartModel getTeamModel() {
		if (teamModel == null) {
			teamModel = new HorizontalBarChartModel();
		}
		return teamModel;
	}

	public void setTeamMode(HorizontalBarChartModel teamModel) {
		this.teamModel = teamModel;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	private Date fromDate;
	private Date toDate;
	private String product;
	private HorizontalBarChartModel employeeModel;
	private HorizontalBarChartModel teamModel;
	private double total;
	private List<InvoiceDto> invoices;
	private Map<String, Double> empMap;
	private Map<Integer, Double> teamMap;

	@Inject InvoiceService invoiceService;

	public void searchBefore() throws IOException {
		invoices = invoiceService.searchInvoiceBeforeDate(toDate);
	}

	public void searchAfter() throws IOException {
		invoices = invoiceService.searchInvoiceAfterDate(fromDate);
	}

	public void searchBeforeAndAfter() throws IOException {
		invoices = invoiceService.searchInvoiceBeforeAfter(fromDate, toDate);
		System.out.println(invoices.size());
	}

	public void search() throws IOException {
		if (fromDate == null && toDate != null) {
			searchBefore();
			System.out.println("before");
		} else if (fromDate != null && toDate == null) {
			searchAfter();
			System.out.println("after");
		} else if (fromDate != null && toDate != null) {
			searchBeforeAndAfter();
			System.out.println("fromandafter");
		}
		buildModels2();
	}

	@Inject CallReportService crService;

	private List<WrStatisticsDto> stats = new ArrayList<WrStatisticsDto>();
	private List<WrStatisticsDto> teamstats = new ArrayList<WrStatisticsDto>();

	public void buildModels2() throws IOException {
		empMap = new HashMap<String, Double>();
		teamMap = new HashMap<Integer, Double>();
		calculateTotal(invoices);

		List<UserDto> sales = userService.getUserByRole("sale");
		for (UserDto sale: sales) {
			double amount = 0;
			for (InvoiceDto invoice: invoices) {
				if (invoice.getSalesperson().getId().equalsIgnoreCase(sale.getId())) {
					amount += invoice.getAmount();
				}
			}
			int deals = invoiceService.countInvoiceByUserAndTime(sale.getId(), fromDate, toDate);
			int calls = crService.countReportByUserAndTime(sale.getId(), fromDate, toDate);
			double percent = Math.round(((double) amount / (double) total) * 10000) / 100;
			stats.add(new WrStatisticsDto(sale, amount, deals, calls, null, percent));
		}

		List<TeamDto> teams = teamService.getAll();
		for (TeamDto team: teams) {
			double amount = 0;
			for (InvoiceDto invoice: invoices) {
				if (invoice.getSalesperson().getTeam() != null && invoice.getSalesperson().getTeam().getSeq() == team.getSeq()) {
					amount += invoice.getAmount();
				}
			}
			// teamMap.put(team.getSeq(), amount);
			List<UserDto> roster = userService.getUserByTeam(team.getSeq());
			int deals = 0;
			int calls = 0;
			for (UserDto user: roster) {
				deals += invoiceService.countInvoiceByUserAndTime(user.getId(), fromDate, toDate);
				calls += crService.countReportByUserAndTime(user.getId(), fromDate, toDate);
			}
			double percent = Math.round(((double) amount / (double) total) * 10000) / 100;
			getTeamstats().add(new WrStatisticsDto(null, amount, deals, calls, team, percent));
		}
		// setTeamStats(new ArrayList<Entry<Integer, Double>>(teamMap.entrySet()));

		showing = true;
		System.out.println("DONE");

	}

	public void buildModels() throws IOException {
		// buildMap();
		empMap = new HashMap<String, Double>();
		teamMap = new HashMap<Integer, Double>();
		employeeModel = new HorizontalBarChartModel();
		teamModel = new HorizontalBarChartModel();

		ChartSeries perEmp = new ChartSeries();
		perEmp.setLabel("Amount");
		List<UserDto> sales = userService.getUserByRole("sale");
		for (UserDto sale: sales) {
			double amount = 0;
			for (InvoiceDto invoice: invoices) {
				if (invoice.getSalesperson().getId().equalsIgnoreCase(sale.getId())) {
					amount += invoice.getAmount();
				}
			}
			perEmp.set(sale.getId(), amount);
			System.out.println(sale.getId() + " " + amount);
		}

		employeeModel.addSeries(perEmp);
		employeeModel.setTitle("Bar Chart");
		employeeModel.setLegendPosition("ne");

		Axis xAxis = employeeModel.getAxis(AxisType.X);
		xAxis.setLabel("Amount");
		Axis yAxis = employeeModel.getAxis(AxisType.Y);
		yAxis.setLabel("Employee");

		ChartSeries perTeam = new ChartSeries();
		perTeam.setLabel("Amount");
		List<TeamDto> teams = teamService.getAll();
		for (TeamDto team: teams) {
			double amount = 0;
			for (InvoiceDto invoice: invoices) {
				if (invoice.getSalesperson().getTeam() != null && invoice.getSalesperson().getTeam().getSeq() == team.getSeq()) {
					amount += invoice.getAmount();
				}
			}
			perTeam.set(team.getName(), amount);
		}
		teamModel.addSeries(perTeam);
		teamModel.setTitle("By Team");
		teamModel.setLegendPosition("ne");

		Axis zAxis = teamModel.getAxis(AxisType.X);
		xAxis.setLabel("Amount");
		Axis tAxis = teamModel.getAxis(AxisType.Y);
		yAxis.setLabel("Team");

		showing = true;
		System.out.println("DONE");
	}

	private void buildMap() throws IOException {
		List<UserDto> sales = userService.getUserByRole("sale");
		empMap = new HashMap<String, Double>();
		teamMap = new HashMap<Integer, Double>();
		for (UserDto user: sales) {
			if (user.getTeam() == null) {
				System.out.println("teamnull");
				System.out.println(user.getId());
			}
			if (user.getTeam() != null) {
				System.out.println(user.getName());
				empMap.put(user.getId(), 0.0);
				teamMap.put(user.getTeam().getSeq(), 0.0);
			}
		}
		System.out.println(empMap.size());
		System.out.println(teamMap.size());
		Iterator it = empMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(((String) pair.getKey()) + " " + (Double)pair.getValue());
			it.remove();
		}
		for (InvoiceDto invoice: invoices) {
			empMap.put(invoice.getSalesperson().getId(), empMap.get(invoice.getSalesperson().getId()) + invoice.getAmount());
			teamMap.put(invoice.getSalesperson().getTeam().getSeq(), teamMap.get(invoice.getSalesperson().getTeam().getSeq()) + invoice.getAmount());
		}
	}

	public void calculateTotal(List<InvoiceDto> invoices) {
		total = 0;
		for (InvoiceDto dto: invoices) {
			total += dto.getAmount();
		}
	}

	public double calculateInvoiceValue(InvoiceDto invoice) {
		double value = 0;
		for (ProductDto prod: invoice.getProducts()) {
			if (prod.getDuration() == 0) {
				value += prod.getPermanentPrice() * prod.getQuantity();
			} else {
				value += prod.getPrice() * prod.getQuantity() * prod.getDuration();
			}
		}
		return value;
	}

	public Map<Integer, Double> getTeamMap() {
		if (teamMap == null) {
			teamMap = new HashMap<Integer, Double>();
		}
		return teamMap;
	}

	public void setTeamMap(Map<Integer, Double> teamMap) {
		this.teamMap = teamMap;
	}

	public Map<String, Double> getEmpMap() {
		if (empMap == null) {
			empMap = new HashMap<String, Double>();
		}
		return empMap;
	}

	public void setEmpMap(Map<String, Double> empMap) {
		this.empMap = empMap;
	}

	public boolean isShowing() {
		return showing;
	}

	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	public List<Entry<String, Double>> getEmpStats() {
		return empStats;
	}

	public void setEmpStats(List<Entry<String, Double>> empStats) {
		this.empStats = empStats;
	}

	public List<Entry<Integer, Double>> getTeamStats() {
		return teamStats;
	}

	public void setTeamStats(List<Entry<Integer, Double>> teamStats) {
		this.teamStats = teamStats;
	}

	public List<WrStatisticsDto> getStats() {
		return stats;
	}

	public void setStats(List<WrStatisticsDto> stats) {
		this.stats = stats;
	}

	public List<WrStatisticsDto> getTeamstats() {
		return teamstats;
	}

	public void setTeamstats(List<WrStatisticsDto> teamstats) {
		this.teamstats = teamstats;
	}

	private boolean showing;

	private List<Entry<String, Double>> empStats;
	private List<Entry<Integer, Double>> teamStats;

	@Inject ManagerController manController;
	@Inject SessionManager sessionManager;
	@Inject SaleTargetService stService;
	@Inject NoteService noteService;

	public void assignSaleTargetAllTeam() throws IOException {
		SaleTargetDto currentTarget = manController.getCurrentTarget();

		if (currentTarget.getFromDate().getTime() > (new Date()).getTime() ||
				currentTarget.getToDate().getTime() <= (new Date()).getTime()) {
			sessionManager.addGlobalMessageFatal("Time period must cover today", null);
			return;
		}
		if (currentTarget.getFromDate().getTime() > currentTarget.getToDate().getTime()) {
			sessionManager.addGlobalMessageFatal("Start date must not be later than end date", null);
			return;
		}
		if (currentTarget.getTarget() <= 0) {
			sessionManager.addGlobalMessageFatal("Invalid target amount.", null);
			return;
		}

		toDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(toDate);
		cal.add(Calendar.MONTH, -3);
		fromDate = cal.getTime();

		search();

		int totalCalls = 0, totalDeals = 0;
		for (WrStatisticsDto wsd: teamstats) {
			totalCalls += wsd.getCalls();
			totalDeals += wsd.getDeals();
		}

		for (WrStatisticsDto wsd: teamstats) {
			TeamDto team = wsd.getTeam();
			List<UserDto> sales = userService.getUserByTeam(team.getSeq());
			int noOfEmp = sales.size();

			int amount = 0;
			if (currentTarget.getAction().equalsIgnoreCase("Generate revenue of")) {
				amount = (int) (currentTarget.getTarget() * wsd.getTotal() / total / noOfEmp);
			} else if (currentTarget.getAction().equalsIgnoreCase("Close deals with")) {
				amount = (int) (currentTarget.getTarget() * wsd.getDeals() / totalDeals / noOfEmp);
			}
			for (UserDto dto: sales) {
				SaleTargetDto std = new SaleTargetDto(dto, currentTarget.getAction(), amount, currentTarget.getFromDate(), currentTarget.getToDate(), 0, currentTarget.getUnit());
				stService.addSaleTarget(std);

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String fromDateStr = sdf.format(currentTarget.getFromDate());
				String toDateStr = sdf.format(currentTarget.getToDate());
				String messageContent = "New sale target has been assigned: " + currentTarget.getAction().toLowerCase() + " " + amount + " " + currentTarget.getUnit() + " from " + fromDateStr + " to " + toDateStr;

				NoteDto message = new NoteDto(noteService.getSeq(), sessionManager.getLoginUser(), dto, messageContent, new Date());
				noteService.addNote(message);
			}
		}
		manController.setAssignTargetMode(false);
		sessionManager.addGlobalMessageInfo("Sale goals assigned", null);
	}

}
