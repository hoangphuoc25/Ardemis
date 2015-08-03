package rd.spec.dataCache;

import java.io.IOException;

import rd.dto.CompanyDto;

public interface CompanyCache {
	public void clear();
	public CompanyDto getCompany(int seq) throws IOException;
	public void put(CompanyDto company);
	public void remove(int seq);
	public CompanyDto search(String name);
}
