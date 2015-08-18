package rd.spec.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import rd.dto.MeetingDto;

public interface MeetingDao {
	public void addMeeting(Transaction transaction, MeetingDto meeting) throws IOException;
	public List<MeetingDto> getMeetingByUser(Transaction transaction, String id) throws IOException;
	public void editMeeting(Transaction transaction, MeetingDto meeting) throws IOException;
	public void deleteMeeting(Transaction transaction, int seq) throws IOException;
	public MeetingDto getById(Transaction transaction, int seq) throws IOException;
	public int getSeq(Transaction transaction) throws IOException;
	public List<MeetingDto> getMeetingToday(Transaction transaction) throws IOException;
	public List<MeetingDto> getMeetingToday(Transaction transaction, String username) throws IOException;
	public List<MeetingDto> getMeetingByDayAndUser(Transaction transaction, String userId, Date date) throws IOException;
	public List<MeetingDto> getByIntervalAndUser(Transaction transaction, Date startDate, Date endDate, String username) throws IOException;
}
