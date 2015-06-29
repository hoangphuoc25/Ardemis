package rd.impl.dataCache;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.CompanyDto;
import rd.spec.dataCache.CompanyCache;

@Stateful
@SessionScoped
public class CompanyCacheImpl implements CompanyCache, Serializable {

	private static final long serialVersionUID = 8850072503399996985L;
	private Map<Integer, CompanyDto> companyMap = new HashMap<Integer, CompanyDto>();

	@Override
	public void clear() {
		companyMap.clear();
	}

	@Override
	public CompanyDto getCompany(int seq) throws IOException {
		return companyMap.get(seq);
	}

	@Override
	public void put(CompanyDto company) {
		companyMap.put(company.getSeq(), company);
	}

	@Override
	public void remove(int seq) {
		companyMap.remove(seq);
	}
}
