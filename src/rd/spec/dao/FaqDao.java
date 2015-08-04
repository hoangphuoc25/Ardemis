package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.FaqDto;

public interface FaqDao {
	public void addFaq(Transaction transaction, FaqDto faq) throws IOException;
	public void updateFaq(Transaction transaction, FaqDto faq) throws IOException;
	public void deleteFaq(Transaction transaction, FaqDto faq) throws IOException;
	public List<FaqDto> getAll(Transaction transaction) throws IOException;
	public List<FaqDto> getByProduct(Transaction transaction, int seq) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public FaqDto getById(Transaction transaction, int seq) throws IOException;
}
