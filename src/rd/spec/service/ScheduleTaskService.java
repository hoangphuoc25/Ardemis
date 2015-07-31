package rd.spec.service;

import java.io.IOException;
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
}