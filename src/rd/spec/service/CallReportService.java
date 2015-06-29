package rd.spec.service;

import java.io.IOException;

import rd.dto.CallReportDto;

public interface CallReportService {
	public void addReport(CallReportDto report) throws IOException;
	public CallReportDto getById(int seq) throws IOException;
	public void updateCallReport(CallReportDto report) throws IOException;
	public void deleteCallReport(CallReportDto cr) throws IOException;
}
