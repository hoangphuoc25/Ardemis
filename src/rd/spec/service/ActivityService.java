package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ActivityDto;
import rd.dto.ProductDto;

public interface ActivityService {
	public void addActivity(ActivityDto act) throws IOException;
	public ActivityDto getById(int seq) throws IOException;
	public List<ActivityDto> getByUser(String username) throws IOException;
	public void updateActivity(ActivityDto act) throws IOException;
	public void deleteActivity(ActivityDto act) throws IOException;
	public int getSeq() throws IOException;
	public List<ActivityDto> findByStatus(String status, String username) throws IOException;
	public List<ActivityDto> getActiveDeal() throws IOException;
	public List<ProductDto> getProductByDeal(int seq) throws IOException;
	public List<ActivityDto> searchByCustomerName(String name) throws IOException;
	public List<ActivityDto> getActiveDealByContact(int seq) throws IOException;
	public List<ActivityDto> getActiveDealByCompany(String company) throws IOException;
	public List<ActivityDto> getActiveDealBySalesperson(String userId) throws IOException;
}
