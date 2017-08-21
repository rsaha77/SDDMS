package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.entity.CalendarEventEntity;

public interface CalendarDao {
	List <CalendarEventEntity> getCalendarEventEntityList () throws IOException;
	void setReminderForLead (CalendarEventEntity calendarEventEntity);
	void setReminderForClient (CalendarEventEntity calendarEventEntity);
	void setReminderForDisq (CalendarEventEntity calendarEventEntity);
}
