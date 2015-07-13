package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.FeedbackDto;

public interface FeedbackDao {
	public List<FeedbackDto> getFeedbackByProduct(Transaction transaction, int seq) throws IOException;
	public FeedbackDto getById(Transaction transaction, int seq) throws IOException;
	public void addFeedback(Transaction transaction, FeedbackDto fb) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
}
