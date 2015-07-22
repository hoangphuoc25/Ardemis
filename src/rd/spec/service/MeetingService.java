package rd.spec.service;

import java.io.IOException;
import java.util.List;

import rd.dto.MeetingDto;

public interface MeetingService {
	public void addMeeting(MeetingDto meeting) throws IOException;
	public List<MeetingDto> getMeetingByUser(String id) throws IOException;
	public void editMeeting(MeetingDto meeting) throws IOException;
	public void deleteMeeting(int seq) throws IOException;
	public MeetingDto getById(int seq) throws IOException;
	public int getSeq() throws IOException;
	public List<MeetingDto> getMeetingToday() throws IOException;
}
