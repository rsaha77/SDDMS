package project.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.CalendarDao;
import project.dao.spec.OrderDao;
import project.dto.OrderDetailsDto;
import project.entity.CalendarEventEntity;

@Repository
public class CalendarDaoImpl implements CalendarDao {
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private OrderDao orderDao;
	
	public List <CalendarEventEntity> getCalendarEventEntityListFromCalendarEventsTable () {
		return template.query (
			"SELECT * from calendarEvents",
			(ps) -> {
				
			},
			(rs, rownum) -> {
				return new CalendarEventEntity (
					rs.getString ("title"),
					rs.getString ("start"),
					rs.getString ("clientId"),
					rs.getString ("productId"),
					rs.getInt ("orderId")
				);
					
			}
		);
	}
	
	public List <CalendarEventEntity> getCalendarEventEntityListFromOrderTable () {
		List <CalendarEventEntity> calEventList = new ArrayList <CalendarEventEntity>();
		try {
			List <OrderDetailsDto> orderDetailsDto = orderDao.getBy (0,0,0);
//			System.out.println (orderDetailsDto.size ());
			for (OrderDetailsDto orderDetails: orderDetailsDto) {
				CalendarEventEntity calEvent = new CalendarEventEntity();
				calEvent.setTitle ("Process OrderId " + orderDetails.getOrderId ());
				calEvent.setStart (orderDetails.getOrderDate ());
				calEvent.setClientId (orderDetails.getClientId ());
				calEvent.setProductId (orderDetails.getProId ());
				calEvent.setOrderId (orderDetails.getOrderId ());
				calEventList.add (calEvent);
			}
		} catch (IOException e) {
			System.out.println ("Yaar Lead pe problem de raha h" +  e);
		}
		return calEventList;
	}
	
	@Override
	public List <CalendarEventEntity> getCalendarEventEntityList () throws IOException {
		try {
			List <CalendarEventEntity> eventsFromCalendarTable = getCalendarEventEntityListFromCalendarEventsTable ();
			List <CalendarEventEntity> eventsFromOrderTable = getCalendarEventEntityListFromOrderTable ();
			List <CalendarEventEntity> newList = eventsFromCalendarTable;
			newList.addAll(eventsFromOrderTable);
			return newList;
				
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	public String convertIntToString (int a) {
		return Integer.toString (a);
	}

	
//	private static final String INSERT_ORDER_DETAILS_TO_CALENDAR_EVENTS = "INSERT into calendarEvents "
//			+ "(title, start, clientId, productId, orderId)"
//			+ "VALUES (?, ?, ?, ?, ?)";
//	
//	@Override
//	public void insertEventAfterPlacingOrder (OrderDetailsCreationEntity orderDetailsCreationEntity) throws IOException {
//		String orderId = convertIntToString (orderDetailsCreationEntity.getOrderId ());
//		try {
//			template.update (
//				INSERT_ORDER_DETAILS_TO_CALENDAR_EVENTS,
//				(ps) -> {
//					ps.setString (1, "Process Order " + orderId);
//					ps.setString (2, orderDetailsCreationEntity.getOrderDate ());
//					ps.setString (3, orderDetailsCreationEntity.getClientId ());
//					ps.setString (4, orderDetailsCreationEntity.getProId ());
//					ps.setInt (5, orderDetailsCreationEntity.getOrderId ());
//				}
//			);
//		} catch (DataAccessException e) {
//			throw new IOException (e);
//		}
//	}
	
	
	private static final String INSERT = "INSERT into calendarEvents "
			+ "(title, start, clientId, productId, orderId)"
			+ "VALUES (?, ?, ?, ?, ?)";
	
	
	// No Deal has happened
	
	@Override
	public void setReminderForLead (CalendarEventEntity calendarEventEntity) {
		template.update (
			INSERT,
			(ps) -> {
				ps.setString (1, calendarEventEntity.getTitle ());
				ps.setString (2, calendarEventEntity.getStart ());
				ps.setString (3, calendarEventEntity.getClientId ());
				ps.setString (4, calendarEventEntity.getProductId ());
				ps.setInt (5, calendarEventEntity.getOrderId ());
			}
		);
	}
	
	//This is while dealing
	
	@Override
	public void setReminderForClient (CalendarEventEntity calendarEventEntity) {
		template.update (
			INSERT,
			(ps) -> {
				ps.setString (1, calendarEventEntity.getTitle ());
				ps.setString (2, calendarEventEntity.getStart ());
				ps.setString (3, calendarEventEntity.getClientId ());
				ps.setString (4, calendarEventEntity.getProductId ());
				ps.setInt (5, calendarEventEntity.getOrderId ());
			}
		);
		
		template.update (
			"UPDATE client SET next_Order_Date = ? WHERE id = ?",
			(ps) -> {
				ps.setString (1, calendarEventEntity.getStart ());
				ps.setString (2, calendarEventEntity.getClientId ());
			}
		);
	}
	
	@Override
	public void setReminderForDisq (CalendarEventEntity calendarEventEntity) {
		template.update (
			INSERT,
			(ps) -> {
				ps.setString (1, calendarEventEntity.getTitle ());
				ps.setString (2, calendarEventEntity.getStart ());
				ps.setString (3, calendarEventEntity.getClientId ());
				ps.setString (4, calendarEventEntity.getProductId ());
				ps.setInt (5, calendarEventEntity.getOrderId ());
			}
		);
	}
	
}











