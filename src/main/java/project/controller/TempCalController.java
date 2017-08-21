package project.controller;

import java.util.List;

//import static java.lang.System.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.dao.spec.CalendarDao;
import project.dao.spec.ClientDao;
import project.entity.*;
import project.service.spec.*;

@Controller
public class TempCalController {
	
	@Autowired
	CalendarService calendarService;
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	CalendarDao calendarDao;
	
	@Autowired
	ClientDao clientDao;
	
	@RequestMapping(value = "/calendar")
	public String calendar() {
		return "calendar";
	}
	
	@RequestMapping (value = "/calendar/getEventsForCalendar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <CalendarEventEntity> getCalendarEventList () {
		List <CalendarEventEntity> calendarEventEntityList = calendarService.getCalendarEventEntityList ();
//		out.println (calendarEventEntityList);
		return calendarEventEntityList;
	}

	@RequestMapping (value = "/reminder/setReminderForLead", method = RequestMethod.POST)
	@ResponseBody
	public void setReminderForLead (@RequestBody CalendarEventEntity calendarEventEntity) {
		calendarDao.setReminderForLead (calendarEventEntity);
		clientDao.setReminderTrueForLeadId (calendarEventEntity.getClientId ());
	}
	
	@RequestMapping (value = "/reminder/setReminderForClient", method = RequestMethod.POST)
	@ResponseBody
	public void setReminderForClient (@RequestBody CalendarEventEntity calendarEventEntity) {
		calendarDao.setReminderForClient (calendarEventEntity);
	}
	
	
	@RequestMapping (value = "/reminder/setReminderForDisq", method = RequestMethod.POST)
	@ResponseBody
	public void setReminderForDisq (@RequestBody CalendarEventEntity calendarEventEntity) {
		calendarDao.setReminderForDisq (calendarEventEntity);
	}
	
	
	@RequestMapping (value = "/reminder/deleteClientReminder", method = RequestMethod.POST)
	@ResponseBody
	public void deleteClientReminder (@RequestParam String clientId) {
		template.update (
			"DELETE from calendarEvents WHERE clientId = ?",
			(ps) -> {
				ps.setString (1, clientId);
			}
		);
	}
	
	
	@RequestMapping (value = "/reminder/deleteLeadReminder", method = RequestMethod.POST)
	@ResponseBody
	public void deleteLeadReminder (@RequestParam String clientId) {
		template.update (
			"DELETE from calendarEvents WHERE clientId = ?",
			(ps) -> {
				ps.setString (1, clientId);
			}
		);
	}
	
	@RequestMapping (value = "/reminder/deleteOrderReminder", method = RequestMethod.POST)
	@ResponseBody
	public void deleteOrderReminder (@RequestParam int orderId) {
		template.update (
			"DELETE from calendarEvents WHERE orderId = ?",
			(ps) -> {
				ps.setInt (1, orderId);
			}
		);
	}
	
}







































