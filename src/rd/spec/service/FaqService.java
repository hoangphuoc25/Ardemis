package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.FaqDto;

public interface FaqService {
	public void addFaq(FaqDto faq) throws IOException;

	public void updateFaq(FaqDto faq) throws IOException;

	public void deleteFaq(FaqDto faq) throws IOException;

	public List<FaqDto> getAll() throws IOException;

	public List<FaqDto> getByProduct(int seq) throws IOException;

	public int getSeq() throws IOException;

	public FaqDto getById(int seq) throws IOException;
}
