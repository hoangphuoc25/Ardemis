package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.ActivityDto;

public interface ActivityService {
	public void addActivity(ActivityDto act) throws IOException;
	public ActivityDto getById(int seq) throws IOException;
	public List<ActivityDto> getByUser(String username) throws IOException;
	public void updateActivity(ActivityDto act) throws IOException;
	public void deleteActivity(ActivityDto act) throws IOException;
	public int getSeq() throws IOException;
	public List<ActivityDto> findByStatus(String status, String username) throws IOException;
}
