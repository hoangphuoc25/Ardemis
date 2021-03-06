package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.CompanyDto;

public interface CompanyDao {
	public void updateCompany(Transaction transaction, CompanyDto company) throws IOException;
	public void deleteCompany(Transaction transaction, int seq) throws IOException;
	public void insertCompany(Transaction transaction, CompanyDto company) throws IOException;
	public CompanyDto getById(Transaction transaction, int seq) throws IOException;
	public List<CompanyDto> getByIndustry(Transaction transaction, String industry) throws IOException;
	public List<CompanyDto> searchCompanyByName(Transaction transaction, String name) throws IOException;
	public List<CompanyDto> getAll(Transaction transaction) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<CompanyDto> getCompanyByContactStatus(Transaction transaction, String status) throws IOException;
	public List<CompanyDto> searchByIndustry(Transaction transaction, String industry) throws IOException;
	public List<CompanyDto> searchByLocation(Transaction transaction, String location) throws IOException;
	public CompanyDto searchCompanyByNameExact(Transaction transaction, String name) throws IOException;
	public List<CompanyDto> getCompanyByContactStatusAndUser(Transaction transaction, String status, String user) throws IOException;
}
