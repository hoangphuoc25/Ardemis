package rd.spec.dao;

import java.io.IOException;
import java.util.List;

import rd.dto.ScheduleTaskDto;

public interface ScheduleTaskDao {
	public int getSeq(Transaction transaction) throws IOException;
	public void addEvent(Transaction transaction, ScheduleTaskDto evt) throws IOException;
	public void updateEvent(Transaction transaction, ScheduleTaskDto evt) throws IOException;
	public void deleteEvent(Transaction transaction, int seq) throws IOException;
	public ScheduleTaskDto getById(Transaction transaction, int seq) throws IOException;
	public List<ScheduleTaskDto> getByUser(Transaction transaction, String username) throws IOException;
	public List<ScheduleTaskDto> getByUserToday(Transaction transaction, String username) throws IOException;
}
