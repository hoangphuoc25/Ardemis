package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.CompanyDto;
import rd.dto.ContactDto;

public interface ContactService {
	public void addContact(ContactDto contact) throws IOException;
	public ContactDto getContactById(int seq) throws IOException;
	public void updateContact(ContactDto contact) throws IOException;
	public void deleteContact(int seq) throws IOException;
	public List<ContactDto> getByCompany(CompanyDto company) throws IOException;
}
