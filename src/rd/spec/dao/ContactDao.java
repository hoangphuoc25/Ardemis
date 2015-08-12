package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.dto.ProductDto;

public interface ContactDao {
	public void addContact(Transaction transaction, ContactDto contact) throws IOException;

	public ContactDto getContactById(Transaction transaction, int seq) throws IOException;

	public void updateContact(Transaction transaction, ContactDto contact) throws IOException;

	public void deleteContact(Transaction transaction, int seq) throws IOException;

	public List<ContactDto> getByCompany(Transaction transaction, CompanyDto company) throws IOException;

	public int getSeq(Transaction transaction) throws IOException;

	public List<ContactDto> searchContactByName(Transaction transaction, String partial) throws IOException;

	public List<ContactDto> getAll(Transaction transaction) throws IOException;

	public List<ContactDto> getByStatusAndUser(Transaction transaction, String status, String userId) throws IOException;

	public List<ContactDto> getByStatus(Transaction transaction, String status) throws IOException;
	public int getNumberOfContactPerSale(Transaction transaction, String saleId) throws IOException;
	public void addCompanyContact(Transaction transaction, ContactDto contact,CompanyDto company) throws IOException;
	public void registerInterest(Transaction transaction, ContactDto contact,List<ProductDto> products) throws IOException;
}
