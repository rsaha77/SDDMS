package project.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.spec.CalendarDao;
import project.entity.CalendarEventEntity;
import project.exception.ServiceException;
import project.service.spec.CalendarService;

@Service
public class CalendarServiceImpl implements CalendarService {
	
	@Autowired
	CalendarDao calendarDao;
	
	@Override
	public List <CalendarEventEntity> getCalendarEventEntityList () throws ServiceException {
		try {
			return calendarDao.getCalendarEventEntityList ();
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
}
