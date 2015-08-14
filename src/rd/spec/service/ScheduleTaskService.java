package rd.spec.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.ScheduleTaskDto;

public interface ScheduleTaskService {
	public int getSeq() throws IOException;
	public void addEvent(ScheduleTaskDto evt) throws IOException;
	public void updateEvent(ScheduleTaskDto evt) throws IOException;
	public void deleteEvent(int seq) throws IOException;
	public ScheduleTaskDto getById(int seq) throws IOException;
	public List<ScheduleTaskDto> getByUser(String username) throws IOException;
	public List<ScheduleTaskDto> getByUserToday(String username) throws IOException;
	public List<ScheduleTaskDto> getByCompany(int seq) throws IOException;
	public List<ScheduleTaskDto> getByContact(int seq) throws IOException;
	public List<ScheduleTaskDto> getByDeal(int seq) throws IOException;
	public List<ScheduleTaskDto> getTaskNextWeekByUser(String userId) throws IOException;
	public List<ScheduleTaskDto> getByUser(String userId, Date date) throws IOException;
	public List<ScheduleTaskDto> getByUserAndStatus(String userId, String status) throws IOException;
}
