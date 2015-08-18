package rd.spec.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.CallReportDto;

public interface CallReportDao {
	public void addReport(Transaction transaction, CallReportDto report) throws IOException;
	public CallReportDto getById(Transaction transaction, int seq) throws IOException;
	public void updateCallReport(Transaction transaction, CallReportDto report) throws IOException;
	public void deleteCallReport(Transaction transaction, CallReportDto cr) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<CallReportDto> getAll(Transaction transaction) throws IOException;
	public List<CallReportDto> getByCompanyId(Transaction transaction, int seq) throws IOException;
	public List<CallReportDto> getByContact(Transaction transaction, int seq) throws IOException;
	public List<CallReportDto> getByDeal(Transaction transaction, int seq) throws IOException;
	public int countReportByUserAndTime(Transaction transaction, String username, Date startDate, Date endDate) throws IOException;
}
