package rd.spec.dataCache;

import rd.dto.ContactDto;

public interface ContactCache {
	public ContactDto getContact(int seq);
	public void put(ContactDto contact);
	public void clear();
	public void removeContact(int seq);
}
