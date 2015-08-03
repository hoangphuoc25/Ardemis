package rd.impl.dataCache;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
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
		if (companyMap.containsKey(seq))
			return companyMap.get(seq);
		else
			return null;
	}

	@Override
	public void put(CompanyDto company) {
		companyMap.put(company.getSeq(), company);
	}

	@Override
	public void remove(int seq) {
		companyMap.remove(seq);
	}

	@Override
	public CompanyDto search(String name) {
	    Iterator it = companyMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
        	CompanyDto dto = (CompanyDto) pair.getValue();
        	if (dto.getName().equalsIgnoreCase(name.toLowerCase())) {
        		return dto;
        	}
	    }
	    return null;
	}
}
