package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.FeedbackDto;

public interface FeedbackDao {
	public List<FeedbackDto> getFeedbackByProduct(Transaction transaction, int seq) throws IOException;
	public FeedbackDto getById(Transaction transaction, int seq) throws IOException;
	public void addFeedback(Transaction transaction, FeedbackDto fb) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public double getAverageFeature(Transaction transaction, int seq) throws IOException;
	public double getAverageThirdPartySupport(Transaction transaction, int seq) throws IOException;
	public double getAveragePerformance(Transaction transaction, int seq) throws IOException;
	public double getAverageUserEx(Transaction transaction, int seq) throws IOException;
	public double getAverageUserIn(Transaction transaction, int seq) throws IOException;
	public double getAverageUsability(Transaction transaction, int seq) throws IOException;
	public double getAverageStability(Transaction transaction, int seq) throws IOException;
	public String getHappiness(Transaction transaction, int contactSeq, int prodSeq) throws IOException;
	public void addClientHappiness(Transaction transaction, int contactSeq, int productSeq, String happiness) throws IOException;
}
