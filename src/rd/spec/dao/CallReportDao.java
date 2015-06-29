package rd.spec.dao;

import java.io.IOException;

import rd.dto.CallReportDto;

public interface CallReportDao {
	public void addReport(Transaction transaction, CallReportDto report) throws IOException;
	public CallReportDto getById(Transaction transaction, int seq) throws IOException;
	public void updateCallReport(Transaction transaction, CallReportDto report) throws IOException;
	public void deleteCallReport(Transaction transaction, CallReportDto cr) throws IOException;
}
