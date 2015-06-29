
package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.SupportTicketDto;

public interface SupportTicketDao {
	public List<SupportTicketDto> getAll(Transaction transaction) throws IOException;
	public List<SupportTicketDto> getPendingTicket(Transaction transaction) throws IOException;
	public List<SupportTicketDto> getTicketByName(Transaction transaction, String name) throws IOException;
	public List<SupportTicketDto> getTicketByCompany(Transaction transaction, String company) throws IOException;
}
