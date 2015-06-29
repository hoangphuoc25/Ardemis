package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.CompanyDto;
import rd.dto.ContactDto;
import rd.spec.dao.ContactDao;
import rd.spec.dao.Transaction;
import rd.spec.service.ContactService;

public class ContactServiceImpl implements ContactService, Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private ContactDao contactDao;

	@Inject
	public ContactServiceImpl(Transaction transaction, ContactDao contactDao) {
		this.transaction = transaction;
		this.contactDao = contactDao;
	}

	public void addContact(ContactDto contact) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			contactDao.addContact(transaction, contact);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public ContactDto getContactById(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			ContactDto contact = contactDao.getContactById(transaction, seq);
			transaction.commit();
			return contact;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void updateContact(ContactDto contact) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			contactDao.updateContact(transaction, contact);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void deleteContact(int seq) throws IOException {
		// ATTENTION: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try {
			transaction.begin();
			contactDao.deleteContact(transaction, seq);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<ContactDto> getByCompany(CompanyDto company) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<ContactDto> result = contactDao.getByCompany(transaction, company);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
