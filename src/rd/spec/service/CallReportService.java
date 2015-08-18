package rd.spec.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.CallReportDto;

public interface CallReportService {
	public void addReport(CallReportDto report) throws IOException;
	public CallReportDto getById(int seq) throws IOException;
	public void updateCallReport(CallReportDto report) throws IOException;
	public void deleteCallReport(CallReportDto cr) throws IOException;
	public int getSeq() throws IOException;
	public List<CallReportDto> getAll() throws IOException;
	public List<CallReportDto> getByCompanyId(int seq) throws IOException;
	public List<CallReportDto> getByContact(int seq) throws IOException;
	public List<CallReportDto> getByDeal(int seq) throws IOException;
	public int countReportByUserAndTime(String username, Date startDate, Date endDate) throws IOException;
}
