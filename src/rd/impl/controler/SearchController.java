package rd.impl.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import rd.dto.CategoryDto;
import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.ProductDto;
import rd.dto.UserDto;
import rd.spec.service.CategoryService;
import rd.spec.service.CompanyService;
import rd.spec.service.ContactService;
import rd.spec.service.ProductService;
import rd.spec.service.UserService;

@Named
@ConversationScoped
public class SearchController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject Conversation conversation;

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

	@Inject CompanyService compService;
	@Inject ProductService prodService;
	@Inject UserService userService;
	@Inject CategoryService catService;
	@Inject ContactService contactService;

	public List<String> suggestCompany(String partial) throws IOException {
		List<String> result = new ArrayList<String>();
		List<CompanyDto> temp = compService.searchCompanyByName(partial);
		for (CompanyDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public List<String> suggestProduct(String partial) throws IOException {
		List<ProductDto> temp = prodService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (ProductDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getSeq() + ")");
		}
		return result;
	}

	public List<String> suggestUser(String partial) throws IOException {
		List<UserDto> temp = userService.searchByName(partial);
		List<String> result = new ArrayList<String>();
		for (UserDto dto: temp) {
			result.add(dto.getName() + "(" + dto.getId() + ")");
		}
		return result;
	}

	public List<String> suggestCategory(String partial) throws IOException {
		List<CategoryDto> temp = catService.searchCategory(partial);
		List<String> result = new ArrayList<String>();
		for (CategoryDto dto: temp) {
			result.add(dto.getCategory());
		}
		return result;
	}

	public List<String> searchController(String partial) throws IOException {
		List<ContactDto> temp = contactService.searchContactByName(partial);
		List<String> result = new ArrayList<String>();
		for (ContactDto dto: temp) {
			result.add(dto.getName() + " - " + dto.getCompany() + "(" + dto.getSeq() + ")");
		}
		return result;
	}
}
