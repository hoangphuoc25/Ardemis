package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ActivityDto;
import rd.dto.ProductDto;

public interface ActivityDao {
	public void addActivity(Transaction transaction, ActivityDto act) throws IOException;
	public ActivityDto getById(Transaction transaction, int seq) throws IOException;
	public List<ActivityDto> getByUser(Transaction transaction, String username) throws IOException;
	public void updateActivity(Transaction transaction, ActivityDto act) throws IOException;
	public void deleteActivity(Transaction transaction, ActivityDto act) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<ActivityDto> findByStatus(Transaction transaction, String status, String username) throws IOException;
	public List<ActivityDto> getActiveDeal(Transaction transaction) throws IOException;
	public List<ProductDto> getProductByDeal(Transaction transaction, int seq) throws IOException;
}
