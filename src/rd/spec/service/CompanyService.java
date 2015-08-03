package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.CompanyDto;

public interface CompanyService {
	public void updateCompany(CompanyDto company) throws IOException;
	public void deleteCompany(int seq) throws IOException;
	public void insertCompany(CompanyDto company) throws IOException;
	public CompanyDto getById(int seq) throws IOException;
	public List<CompanyDto> getByIndustry(String industry) throws IOException;
	public List<CompanyDto> searchCompanyByName(String name) throws IOException;
	public List<CompanyDto> getAll() throws IOException;
	public int getSeq() throws IOException;
	public List<CompanyDto> getCompanyByContactStatus(String status) throws IOException;
	public List<CompanyDto> searchByIndustry(String industry) throws IOException;
	public List<CompanyDto> searchByLocation(String location) throws IOException;
	public CompanyDto searchCompanyByNameExact(String name) throws IOException;
}
