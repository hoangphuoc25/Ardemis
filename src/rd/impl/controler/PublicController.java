package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.ContactDto;
import rd.dto.UserDto;
import rd.dto.WrUserDto;
import rd.spec.manager.SessionManager;
import rd.spec.service.ContactService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class PublicController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;
	@Inject UserService userService;
	@Inject ContactService contactService;
	@Inject SessionManager sessionManager;

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

	public void reload() {
		conversationBegin();
	}

	public ContactDto getNewCustomer() {
		if (newCustomer == null) {
			newCustomer = new ContactDto();
		}
		return newCustomer;
	}

	public void setNewCustomer(ContactDto newCustomer) {
		this.newCustomer = newCustomer;
	}

	private ContactDto newCustomer;

	public void addNewCustomer() throws IOException {
		if (newCustomer.getCompany() == null || newCustomer.getCompany().isEmpty() || newCustomer.getName() == null || newCustomer.getName().isEmpty()
				|| newCustomer.getPhone() == null || newCustomer.getPhone().isEmpty() || newCustomer.getAddress() == null || newCustomer.getAddress().isEmpty()) {
			sessionManager.addGlobalMessageFatal("Please fill in all required information", null);
			return;
		}
		if (!validatePhone(newCustomer.getPhone())) {
			sessionManager.addGlobalMessageFatal("Malformed phone number. Please include full nation code.", null);
			return;
		}

		newCustomer.setAssignee(getWrSales().get(0).getSale());
		contactService.addContact(newCustomer);
		wrSales.get(0).current++;
		Collections.sort(wrSales, new Comparator<WrUserDto>() {
			public int compare(WrUserDto f1, WrUserDto f2) {
				if (f1.current == f2.current) {
					return 0;
				} else if (f1.current > f2.current) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		newCustomer = new ContactDto();
		sessionManager.addGlobalMessageInfo("Your information has been recorded. Our representative will contact you soon", null);
	}

	public List<WrUserDto> getWrSales() throws IOException {
		if (wrSales == null || wrSales.size() == 0) {
			System.out.println("PublicController.getWrSales()");
			if (userService == null) {
				System.out.println("userService is null");
			}
			List<UserDto> sales = userService.getUserByRole("sale");
			for (UserDto dto: sales) {
				wrSales.add(new WrUserDto(dto, contactService.getNumberOfContactPerSale(dto.getId())));
			}
			Collections.sort(wrSales, new Comparator<WrUserDto>() {
				public int compare(WrUserDto f1, WrUserDto f2) {
					if (f1.getAssignedContacts() == f2.getAssignedContacts()) {
						return 0;
					} else if (f1.getAssignedContacts() > f2.getAssignedContacts()) {
						return 1;
					} else {
						return -1;
					}
				}
			});
			System.out.println(wrSales.size());
		}
		return wrSales;
	}

	public void setWrSales(List<WrUserDto> wrSales) {
		this.wrSales = wrSales;
	}

	private List<WrUserDto> wrSales = new ArrayList<WrUserDto>();

	private boolean validatePhone(String p) {
		String phone = p.replace(" ", "");
		String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		if (!matcher.matches()) {
			return false;
		} else {
			return true;
		}
	}
}
