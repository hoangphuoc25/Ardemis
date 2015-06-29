package rd.impl.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import rd.dto.SupportTicketDto;
import rd.spec.dao.SupportTicketDao;
import rd.spec.dao.Transaction;
import rd.spec.service.SupportTicketService;

public class SupportTicketServiceImpl implements SupportTicketService,
		Serializable {
	private static final long serialVersionUID = 4822474486634242542L;
	private Transaction transaction;
	private SupportTicketDao supportTicketDao;

	@Inject
	public SupportTicketServiceImpl(Transaction transaction, SupportTicketDao supportTicketDao) {
		this.transaction = transaction;
		this.supportTicketDao = supportTicketDao;
	}
	public List<SupportTicketDto> getAll() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<SupportTicketDto> result = supportTicketDao.getAll(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<SupportTicketDto> getPendingTicket() throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<SupportTicketDto> result = supportTicketDao.getPendingTicket(transaction);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<SupportTicketDto> getTicketByName(String name) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<SupportTicketDto> result = supportTicketDao.getTicketByName(transaction, name);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
	public List<SupportTicketDto> getTicketByCompany(String company) throws IOException {
		// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE
		try{
			transaction.begin();
			List<SupportTicketDto> result = supportTicketDao.getTicketByCompany(transaction, company);
			transaction.commit();
			return result;
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}
}
