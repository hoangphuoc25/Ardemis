package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.ProductDto;

public interface ContactService {
	public void addContact(ContactDto contact) throws IOException;

	public ContactDto getContactById(int seq) throws IOException;

	public void updateContact(ContactDto contact) throws IOException;

	public void deleteContact(int seq) throws IOException;

	public List<ContactDto> getByCompany(CompanyDto company) throws IOException;
	public int getSeq() throws IOException;
	public List<ContactDto> searchContactByName(String partial) throws IOException;
	public List<ContactDto> getAll() throws IOException;
	public List<ContactDto> getByStatusAndUser(String status, String userId) throws IOException;
	public List<ContactDto> getByStatus(String status) throws IOException;
	public int getNumberOfContactPerSale(String saleId) throws IOException;
	public void addCompanyContact(ContactDto contact,CompanyDto company) throws IOException;
	public void registerInterest(ContactDto contact,List<ProductDto> products) throws IOException;
	public ContactDto getContactByUserId(String id) throws IOException;
}
