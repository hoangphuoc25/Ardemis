package rd.spec.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.MeetingDto;
import rd.dto.ScheduleTaskDto;
import rd.dto.UserDto;

public interface MeetingService {
	public void addMeeting(MeetingDto meeting) throws IOException;
	public List<MeetingDto> getMeetingByUser(String id) throws IOException;
	public void editMeeting(MeetingDto meeting) throws IOException;
	public void deleteMeeting(int seq) throws IOException;
	public MeetingDto getById(int seq) throws IOException;
	public int getSeq() throws IOException;
	public List<MeetingDto> getMeetingToday() throws IOException;
	public List<MeetingDto> getMeetingToday(String username) throws IOException;
	public List<MeetingDto> getMeetingByDayAndUser(String userId, Date date) throws IOException;
	public List<MeetingDto> getByIntervalAndUser(Date startDate, Date endDate, String username) throws IOException;
	public List<UserDto> searchUserFreeInTimeInterval(Date from, Date to) throws IOException;
	public List<UserDto> searchUserFreeInTimeInterval(Date from, Date to, List<MeetingDto> list) throws IOException;
	public List<UserDto> searchUserFreeInTimeIntervalTask(Date from, Date to, List<ScheduleTaskDto> list) throws IOException;
}
