package project.service.spec;

import java.util.List;

import project.entity.CalendarEventEntity;
import project.exception.ServiceException;

public interface CalendarService {
	List <CalendarEventEntity> getCalendarEventEntityList () throws ServiceException;
}
