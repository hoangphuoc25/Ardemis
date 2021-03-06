package rd.spec.dao;

import java.io.IOException;
import java.util.Date;
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
	public List<ScheduleTaskDto> getByCompany(Transaction transaction, int seq) throws IOException;
	public List<ScheduleTaskDto> getByContact(Transaction transaction, int seq) throws IOException;
	public List<ScheduleTaskDto> getByDeal(Transaction transaction, int seq) throws IOException;
	public List<ScheduleTaskDto> getTaskNextWeekByUser(Transaction transaction, String userId) throws IOException;
	public List<ScheduleTaskDto> getByUser(Transaction transaction, String userId, Date date) throws IOException;
	public List<ScheduleTaskDto> getByUserAndStatus(Transaction transaction, String userId, String status) throws IOException;
	public List<ScheduleTaskDto> getByIntervalAndUser(Transaction transaction, Date fromDate, Date toDate, String username) throws IOException;
}
