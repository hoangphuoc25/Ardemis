package rd.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.glassfish.api.Async;
import org.quartz.JobExecutionException;

import rd.dto.ContactDto;
import rd.dto.InvoiceDto;
import rd.dto.ScheduleTaskDto;
import rd.dto.UserDto;
import rd.spec.service.InvoiceService;
import rd.spec.service.ScheduleTaskService;
import rd.spec.service.UserService;

@Singleton
public class HelloJob {
	@Inject ScheduleTaskService stService;
	@Inject UserService userService;
	@Inject InvoiceService invoiceService;

	public HelloJob() {
	}

	@Schedule(dayOfWeek="Mon")
	public void execute() throws JobExecutionException {
		System.out.println("JSF 2 + Quartz 2 example");

		if (userService == null) {
			System.out.println("userService null");
		}

		try {
			List<UserDto> sales = userService.getUserByRole("sale");
			for (UserDto sale : sales) {
				List<ScheduleTaskDto> nextWeekTask = stService.getTaskNextWeekByUser(sale.getId());
				sendReminderEmail(sale, nextWeekTask);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendReminderEmail(UserDto user, List<ScheduleTaskDto> tasks) {
		final String username = "hoangphuoc25@gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		final String password = "c2p3*9Er";
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("hoangphuoc25@gmail.com"));
			System.out.println("userEmail: " + user.getEmail());
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
			message.setSubject("Testing Subject");
			String content = "";
			content += "Dear " + user.getName() + ",\n\n";
			content += "Here is the list of task you have for next week: \n\n";
			for (int i = 0; i < tasks.size(); i++) {
				content += tasks.get(i).getCategory() + ": "
						+ tasks.get(i).getContact().getName() + "("
						+ tasks.get(i).getContact().getPhone() + ")" + ": "
						+ tasks.get(i).getDetail() + "\n\n";
			}
			content += "\n\nBest regards,";
			message.setText(content);
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Schedule
	public void remindCustomer() throws IOException {
		List<InvoiceDto> expiring = invoiceService.getExpiringPurchase();
		sendEmailExpiringPurchase(expiring);
	}

	public void sendEmailExpiringPurchase(List<InvoiceDto> expiring) {
		final String username = "hoangphuoc25@gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		final String password = "c2p3*9Er";
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		for (InvoiceDto dto: expiring) {
			ContactDto customer = dto.getContact();
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("hoangphuoc25@gmail.com"));
				System.out.println("userEmail: " + customer.getEmail());
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
				message.setSubject("Testing Subject");
				String content = "";
				content += "Dear " + customer.getName() + ",\n\n";
				content += "Your purchase is expiring next week. If you want to continue using our products, please contact us for further assistance.";
				content += "\n\nBest regards,";
				message.setText(content);
				Transport.send(message);
				System.out.println("Done");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Schedule
	public void moveOverdueTask() throws IOException {
		List<UserDto> sales = userService.getUserByRole("sale");
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		for (UserDto sale : sales) {
			List<ScheduleTaskDto> tasks = stService.getByUser(sale.getId(), cal.getTime());
			for (ScheduleTaskDto stDto: tasks) {
				if (stDto.getStatus().equalsIgnoreCase("Pending")) {
					stDto.setStatus("Overdue");
					stService.updateEvent(stDto);
				}
			}
		}
	}
}
