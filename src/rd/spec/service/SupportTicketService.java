package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.SupportTicketDto;

public interface SupportTicketService {
	public List<SupportTicketDto> getAll() throws IOException;
	public List<SupportTicketDto> getPendingTicket() throws IOException;
	public List<SupportTicketDto> getTicketByName(String name) throws IOException;
	public List<SupportTicketDto> getTicketByCompany(String company) throws IOException;
}
