package project.dao.impl;

import java.io.IOException;

import static java.lang.System.out;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.OrderHistoryDao;
import project.dto.OrderHistoryDto;
import project.entity.OrderProcessingCreationEntity;

@Repository
public class OrderHistoryDaoImpl implements OrderHistoryDao {
	@Autowired
	private JdbcTemplate template;
	
	private static final String INSERT_ORDER_HISTORY = "INSERT INTO orderHistory "
			+ " (status, orderId, clientId, clientName, clientContact, clientAddress, productId, productName, orderQuantity, deliveryDateExpected, deliveryDateActual, `delayed`, orderCancellationReason)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	public String getClientContact (String clientId) {
		return template.queryForObject (
			"Select * from client where id = ?",
			(rs, rownum) -> {return rs.getString ("Contact");},
			clientId
		);
	}
	
	public String getClientAddress (String clientId) {
		return template.queryForObject (
			"Select * from client where id = ?",
			(rs, rownum) -> {return rs.getString ("Address");},
			clientId
		);
	}
	
	public String getExpectedDate (int orderId) {
		return template.queryForObject (
			"Select * from orders where orderId = ?",
			(rs, rownum) -> {return  rs.getString ("orderDate");},
			orderId
		);
	}
	
	public String getDelayedValue (String expectedDate, String actualDate) {
		int d2 = (Character.getNumericValue (actualDate.charAt (8))) * 10 + (Character.getNumericValue (actualDate.charAt (9)));
		int d1 = (Character.getNumericValue (expectedDate.charAt (8))) * 10 + (Character.getNumericValue (expectedDate.charAt (9)));
		out.println (actualDate + " -- " + expectedDate);
		out.println (d2 + " -- " + d1);
		return Integer.toString (d2 - d1) + " ";
	}
	
	@Override
	public void insert (OrderProcessingCreationEntity orderProcessingCreationEntity, String status, boolean flag) throws IOException {
		String clientContact = getClientContact (orderProcessingCreationEntity.getClientId());
		String clientAddress = getClientAddress (orderProcessingCreationEntity.getClientId());
		
		String expectedDate = getExpectedDate (orderProcessingCreationEntity.getOrderId ());
		String actualDate = "N / A";
		if (flag == true) {
			actualDate = orderProcessingCreationEntity.getOrderDate();
		}
		String actualDate_ = actualDate;
		String delayed = "N / A";
		if (flag == true) { // PROCESSED
			delayed = getDelayedValue (expectedDate, actualDate);
			delayed = delayed + "day" + ((delayed.charAt (0) == '1' && delayed.charAt (1) == ' ') ? "" : "s");
			if (delayed.charAt (0) == '0') {
				delayed = "ON TIME";
			}
		}
		
		String orderCancellationReason = "N / A";
		if (flag == false) { // CANCELLED
			if (orderProcessingCreationEntity.getReasonManual ().equals ("others")) {
				orderCancellationReason = orderProcessingCreationEntity.getReasonOther ();
			} else {
				orderCancellationReason = orderProcessingCreationEntity.getReasonManual ();
			}
		}
		
		String delayed_ = delayed;
		String orderCancellationReason_ = orderCancellationReason;
		try {
			template.update (INSERT_ORDER_HISTORY,
				(ps) -> {
					ps.setString (1, status);
					ps.setInt (2, orderProcessingCreationEntity.getOrderId ());
					ps.setString (3, orderProcessingCreationEntity.getClientId ());
					ps.setString (4, orderProcessingCreationEntity.getClientName ());
					ps.setString (5, clientContact);
					ps.setString (6, clientAddress);
					ps.setString (7, orderProcessingCreationEntity.getProductId ());
					ps.setString (8, orderProcessingCreationEntity.getProductName ());
					ps.setString (9, orderProcessingCreationEntity.getOrderQuantity ());
					ps.setString (10, expectedDate);
					ps.setString (11, actualDate_);
					ps.setString (12, delayed_);
					ps.setString (13, orderCancellationReason_);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public List <OrderHistoryDto> getBy (int id, int size, int start) throws IOException {
		try {
			return template.query (
				"Select * from orderHistory",
				ps -> {
				},
				(rs, rownum) -> {
					String status = rs.getString ("status");
					if (status.equals ("CANCELLED")) {
						status = "<span class='label label-danger'> <span class='glyphicon glyphicon-remove-sign'> CANCELLED </span>";
					} else {
						status = "<span class='label label-success'> <span class='glyphicon glyphicon-ok'> PROCESSED </span>";
					}
					String delayed = rs.getString ("delayed");
					if (delayed.charAt (0) == 'O') {
						delayed = "<span class='label label-success'> " + delayed + " </span>";
					} else if (delayed.charAt (0) != 'N') {
						delayed = "<span class='label label-warning'> " + delayed + " </span>";
					}
					return new OrderHistoryDto (
						status,
						rs.getInt ("orderId"),
						rs.getString ("clientId"),
						rs.getString ("clientName"),
						rs.getString ("clientContact"),
						rs.getString ("clientAddress"),
						rs.getString ("productId"),
						rs.getString ("productName"),
						rs.getString ("orderQuantity"),
						rs.getString ("deliveryDateExpected"),
						rs.getString ("deliveryDateActual"),
						delayed,
						rs.getString ("orderCancellationReason")
					);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public int getTotalCount (int id) throws IOException {
		try {
			if (id <= 0) {
				return template.queryForObject (
					"Select COUNT(*) from orderHistory",
					(rs, rownum) -> {return rs.getInt(1);}
				);
			} else {
				return template.queryForObject (
					"SELECT COUNT(*) FROM orderHistory WHERE c = ?",
					(rs, rownum) -> {return rs.getInt(1);},
					id
				);
			}
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public void deleteBy (String orderId) throws IOException {
		try {
			template.update (
				"Delete from orderHistory where orderId = ?",
				(ps) -> ps.setString (1, orderId)
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
}


















