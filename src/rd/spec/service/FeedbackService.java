package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.FeedbackDto;

public interface FeedbackService {
	public List<FeedbackDto> getFeedbackByProduct(int seq) throws IOException;
	public FeedbackDto getById(int seq) throws IOException;
	public void addFeedback(FeedbackDto fb) throws IOException;
	public int getSeq() throws IOException;
	public double getAverageFeature(int seq) throws IOException;
	public double getAverageThirdPartySupport(int seq) throws IOException;
	public double getAveragePerformance(int seq) throws IOException;
	public double getAverageUserEx(int seq) throws IOException;
	public double getAverageUserIn(int seq) throws IOException;
	public double getAverageUsability(int seq) throws IOException;
	public double getAverageStability(int seq) throws IOException;
	public String getHappiness(int contactSeq, int prodSeq) throws IOException;
	public void addClientHappiness(int contactSeq, int productSeq, String happiness) throws IOException;
}
