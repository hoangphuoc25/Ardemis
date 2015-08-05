package rd.impl.dataCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import rd.dto.ContactDto;
import rd.spec.dataCache.ContactCache;

@Stateful
@SessionScoped
public class ContactCacheImpl implements ContactCache, Serializable {

	private static final long serialVersionUID = 8850072503399996985L;
	private Map<Integer, ContactDto> contactMap = new HashMap<Integer, ContactDto>();

	@Override
	public ContactDto getContact(int seq) {
		if (contactMap.containsKey(seq))
			return contactMap.get(seq);
		else
			return null;
	}

	@Override
	public void put(ContactDto contact) {
		contactMap.put(contact.getSeq(), contact);
	}

	@Override
	public void clear() {
		contactMap.clear();
	}

	@Override
	public void removeContact(int seq) {
		contactMap.remove(seq);
	}

}
