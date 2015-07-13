package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.FeedbackDto;

public interface FeedbackService {
	public List<FeedbackDto> getFeedbackByProduct(int seq) throws IOException;
	public FeedbackDto getById(int seq) throws IOException;
	public void addFeedback(FeedbackDto fb) throws IOException;
	public int getSeq() throws IOException;
}
