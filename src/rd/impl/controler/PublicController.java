package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		List<WrUserDto> wrSales = new ArrayList<WrUserDto>();
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
		newCustomer.setAssignee(wrSales.get(0).getSale());
		contactService.addContact(newCustomer);
		newCustomer = new ContactDto();
		sessionManager.addGlobalMessageInfo("Your information has been recorded. Our representative will contact you soon", null);
	}
}
