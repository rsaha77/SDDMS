package project.dao.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.OrderDao;
import project.dao.spec.RecentActivityDao;
import project.dto.ClientDetailsDto;
import project.dto.OrderDetailsDto;
import project.entity.LeadDetailsEntity;
import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.MultipleOrdersEntity;
import project.entity.OrderDetailsEntity;
import project.entity.OrderDetailsEntityForView;
import project.entity.OrderItemEntity;
import project.entity.ReportProductShortageInfoEntity;


@Repository
public class OrderDaoImpl implements OrderDao {
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private RecentActivityDao recentActivityDao;
	
	private static final String INSERT_ORDER = "INSERT INTO ORDERS "
			+ " (clientId, clientName, productId, productName, orderQuantity, orderDate, status)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	
	public int convertStringToInt (String s) {
		int ret = 0;
		for (int i = 0; i < s.length (); ++i) {
			ret = ret * 10 + Character.getNumericValue (s.charAt (i));
		}
		return ret;
	}
	
	public String convertIntToString (int a) {
		return Integer.toString (a);
	}
	
	public String getProductCountFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("available");},
			productId
		);
	}
	
	public String getProductOrderedFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("ordered");},
			productId
		);
	}
	
	public String getProductRemainingFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("remaining");},
			productId
		);
	}
	
	public String getTransportFromClientTable (String clientId) {
		return template.queryForObject (
			"SELECT * from client WHERE Id = ?",
			(rs, rownum) -> {return rs.getString ("transport");},
			clientId
		);
	}
	
	public String getProductShortageStatus (String productId) {
		String ret = "AVAILABLE";
		String productRemaining = getProductRemainingFromProductTable (productId);
		if (productRemaining.charAt (0) == '-') {
			ret = "OUT OF STOCK";
		}
		return ret;
	}
	
	public int calculateOutOfStock (String productId) {
		String productRemaining = getProductRemainingFromProductTable (productId);
		int ret = 0;
		for (int i = 1; i < productRemaining.length (); ++i) {
			ret = ret * 10 + Character.getNumericValue (productRemaining.charAt (i));
		}
		return ret;
	}
	
	public String getInstance () {
		DateFormat df = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();   
		return df.format(today);
	}
	
	public int countInLeadFor (String clientId) {
		return template.queryForObject (
			"Select count(*) from lead where clientId = ?",
			(rs, rownum) ->{
				return rs.getInt(1);
			},
			clientId
		);
	}
	
	public boolean isLead (String clientId) {
		if (countInLeadFor (clientId) == 1) {
			return true;
		}
		return false;
	}
	
	public LeadDetailsEntity getLeadEntityForLeadId (String clientId) {
		return template.queryForObject (
			"Select * from lead where clientId = ?",
			(rs, rownum) -> {
				return new LeadDetailsEntity (
					clientId,
					rs.getString ("leadName"), 
					rs.getString  ("leadContact"),
					rs.getString  ("leadEmail"),
					rs.getString  ("leadAddress"),
					rs.getString  ("leadType"),
					rs.getString  ("leadTransport"),
					rs.getString  ("leadRegion"),
					rs.getString  ("reminderSet")
				);
			},
			clientId
		);
	}
	
	
	private static final String INSERT_CLIENT = "INSERT INTO CLIENT "
			+ " (id, email, name, type, address, contact, transport, next_order_date, important_note, region)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public void insertLeadIntoClientTable (String clientId) throws IOException {
		LeadDetailsEntity leadDetailsEntity = getLeadEntityForLeadId (clientId);
		ClientDetailsDto clientDetailsDto = new ClientDetailsDto ();
		
		clientDetailsDto.setClientId (leadDetailsEntity.getClientId ());
		clientDetailsDto.setName (leadDetailsEntity.getLeadName ());
		clientDetailsDto.setContact (leadDetailsEntity.getLeadContact ());
		clientDetailsDto.setEmail (leadDetailsEntity.getLeadEmail ());
		clientDetailsDto.setAddress (leadDetailsEntity.getLeadAddress ());
		clientDetailsDto.setType (leadDetailsEntity.getLeadType ());
		clientDetailsDto.setTransport (leadDetailsEntity.getLeadTransport ());
		clientDetailsDto.setRegion (leadDetailsEntity.getLeadRegion ());
//		System.out.println ("==> " + clientDetailsDto);
		
		template.update (INSERT_CLIENT, (ps) -> {
			ps.setString (1, clientDetailsDto.getClientId ());
			ps.setString (2, clientDetailsDto.getEmail ());
			ps.setString (3, clientDetailsDto.getName ());
			ps.setString (4, clientDetailsDto.getType ());
			ps.setString (5, clientDetailsDto.getAddress());
			ps.setString (6, clientDetailsDto.getContact());
			ps.setString (7, clientDetailsDto.getTransport());
			ps.setString (8, clientDetailsDto.getNextOrderDate());
			ps.setString (9, clientDetailsDto.getImportantNote());
			ps.setString (10, clientDetailsDto.getRegion());
		});
	}
	
	public void deleteLeadFromLeadTable (String clientId) {
		template.update (
			"DELETE from lead where clientId = ?",
			(ps) -> {
				ps.setString (1, clientId);
			}
		);
	}
	
	@Override
	public void placeOrder (OrderDetailsDto orderDetails) throws IOException {
		String clientId = orderDetails.getClientId ();
		// If the client is a new client (Lead) ==> [Insert it into client table and Delete him from lead table ]
		if (isLead (clientId)) {
			insertLeadIntoClientTable (clientId);
			deleteLeadFromLeadTable (clientId);
		}
		try {
			template.update (
				INSERT_ORDER,
				(ps) -> {
					String status = getProductShortageStatus (orderDetails.getProId ());
					ps.setString (1, clientId);
					ps.setString (2, orderDetails.getClientName ());
					ps.setString (3, orderDetails.getProId ());
					ps.setString (4, orderDetails.getProductName ());
					ps.setString (5, orderDetails.getOrderQuantity ());
					ps.setString (6, orderDetails.getOrderDate ());
					ps.setString (7, status);
				}
			);
		} catch (DataAccessException e) {
			System.out.println ("Exception inside the insert function of orderDao\n");
			throw new IOException (e);
		}
		
		// Add Order Quantity to the quantitySold of the ProductTable
//		System.out.println ("Ordered ======> " + convertStringToInt (orderDetails.getOrderQuantity ()));
		template.update (
			"UPDATE product SET quantitySold = quantitySold + ? WHERE productId = ?",
			(ps) -> {
				ps.setInt (1, convertStringToInt (orderDetails.getOrderQuantity ()));
				ps.setString (2, orderDetails.getProId());
			}
		);
		
		recentActivityDao.insert ("Placed", "an order for the product(" + orderDetails.getProductName () + ", " + orderDetails.getProId () + ") ordered by the client (" + orderDetails.getClientName () + ", " + orderDetails.getClientId () + ")", getInstance ());
	}
	
	
	
	private static final String INSERT_ORDER_MULTIPLE = "INSERT INTO orderDetailsForOrderId"
			+ " (orderId, productId, productQuantity)"
			+ " VALUES (?, ?, ?)";
	
	
	@Override
	public void placeOrderMultiple (MultipleOrdersEntity multipleOrdersEntity) {
		
		// Generate an orderId
		template.update (
			"INSERT into orderAndClient (clientName) VALUES (?)",
			(ps) -> {
				ps.setString(1, multipleOrdersEntity.getClientName ());
			}
		);
		
		
		// Now get the orderId
		int orderId = template.queryForObject(
						"SELECT * from orderAndClient order by orderId desc LIMIT 1",
						(rs, rownum) -> {
							return rs.getInt (1);
						}				
					  );
		
		
		
		
		// Now Insert the orders
		
		OrderItemEntity items[] = multipleOrdersEntity.getItems();
		
		for (int ii = 0; ii < items.length; ii++) {
			int i = ii;
			template.update (
				INSERT_ORDER_MULTIPLE,
				(ps) -> {
					ps.setInt (1, orderId);
					ps.setString (2, items[i].getProductId ());
					ps.setInt (3, items[i].getProductQuantity ());
				}
			);
		}
		
		
		// Finally Insert into calendarEvents
		
		template.update (
			"INSERT into calendarEvents (title, start, clientId, orderId) values (?, ?, ?, ?)",
			(ps) -> {
				ps.setString (1, "Process OrderId " + convertIntToString (orderId));
				ps.setString (2, multipleOrdersEntity.getOrderDate());
				ps.setString (3, multipleOrdersEntity.getClientId());
				ps.setInt (4, orderId);
			}
		);
	}
	
	
	
	
	
	@Override
	public List <OrderDetailsDto> getBy (int id, int size, int start) throws IOException {
		try {
			return template.query (
				"SELECT * FROM orders",
				ps -> {
				},
				(rs, rownum) -> {
					String transport = getTransportFromClientTable (rs.getString ("clientId"));
					String statusGenerated = "<span class='label label-success'> <span class='glyphicon glyphicon-ok'> AVAILABLE </span>";
					String statusFromOrderTable = rs.getString ("status");
					String outOfStockBy = convertIntToString (calculateOutOfStock (rs.getString ("productId")));
					if (statusFromOrderTable.equals ("OUT OF STOCK")) {
						statusGenerated = "<span class='label label-danger'> OUT OF STOCK </span>" + "<div class='clearfix'></div>" +"<span class='label label-warning'> by " + outOfStockBy + " items </span>";
					}
					return new OrderDetailsDto (
						rs.getInt ("orderId"),
						rs.getString ("clientId"),
						rs.getString ("clientName"),
						rs.getString ("productId"),  // Name should be same as the column name in the database, order of call to the DB doesn't matter to the DB, but matters to the ObjectDetailsCreationEntity & OrderDetailsDto attributes ordering..
						rs.getString ("productName"),
						rs.getString ("orderQuantity"),
						rs.getString ("orderDate"),
						transport,
						statusGenerated
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
					"SELECT COUNT(*) FROM orders",
					(rs, rownum) -> {
						return rs.getInt(1);
					}
				);
			} else {
				return template.queryForObject (
					"SELECT COUNT(*) FROM orders WHERE c = ?",
					(rs, rownum) -> {
						return rs.getInt(1);
					}, id);
			}
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	
	@Override
	public void deleteBy (int orderId) throws IOException {
		try {
			template.update (
				"DELETE FROM orders where orderId = ?",
				(ps) -> ps.setInt (1, orderId)
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	
	public void updateAvailabilityStatusOfTheProductInTheOrderTable (String status, int orderId) {
		template.update (
			"UPDATE orders SET status = ? WHERE orderId = ?",
			(ps) -> {
				ps.setString (1, status);
				ps.setInt (2, orderId);
			}
		);
	}
	
	
	public void checkAvailabilityStatusOftheProduct (int orderId, String productId) {
		String remaining = getProductRemainingFromProductTable (productId);
		if (remaining.charAt (0) == '-') {
			updateAvailabilityStatusOfTheProductInTheOrderTable ("OUT OF STOCK", orderId);
		}
	}
	
	
	@Override
	public void updateOrderQuantityAfterReceivingRequestFromSalesperson (ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) throws IOException {
		try {
			template.update (
				"UPDATE orders SET orderQuantity = ? WHERE orderID = ?",
				(ps) -> {
					ps.setString (1, modifyOrderQuantityCreationEntity.getOrderQuantityNew ());
					ps.setInt (2, modifyOrderQuantityCreationEntity.getOrderId ());
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		
		checkAvailabilityStatusOftheProduct (modifyOrderQuantityCreationEntity.getOrderId (), modifyOrderQuantityCreationEntity.getProductId ());
	}
	
	
	@Override
	public void reportProductShortageInfo (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws IOException {
		try {
			template.update (
				"UPDATE productShortage SET status = ?, noteToTheManager = ?, clientsAffected = ? WHERE productId = ?",
				(ps) -> {
					ps.setString (1, "URGENT");
					ps.setString (2, reportProductShortageInfoEntity.getNoteToTheManager ());
					ps.setString (3, reportProductShortageInfoEntity.getClientAffected ());
					ps.setString (4, reportProductShortageInfoEntity.getProductId ());
					
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	
	public String getClientTypeForClientName (String clientName) {
		return template.queryForObject (
			"SELECT * from client WHERE name = ?",
			(rs, rownum) -> {
				return rs.getString("type");
			},
			clientName
		);
	}
	
	public String getClientTransportForClientName (String clientName) {
		return template.queryForObject (
			"SELECT * from client WHERE name = ?",
			(rs, rownum) -> {
				return rs.getString("transport");
			},
			clientName
		);
	}
	
	
	public String getClientContactForClientName (String clientName) {
		return template.queryForObject (
			"SELECT * from client WHERE name = ?",
			(rs, rownum) -> {
				return rs.getString("contact");
			},
			clientName
		);
	}
	
	
	public String getClientEmailForClientName (String clientName) {
		return template.queryForObject (
			"SELECT * from client WHERE name = ?",
			(rs, rownum) -> {
				return rs.getString("email");
			},
			clientName
		);
	}
	
	public String getClientAddressForClientName (String clientName) {
		return template.queryForObject (
			"SELECT * from client WHERE name = ?",
			(rs, rownum) -> {
				return rs.getString("address");
			},
			clientName
		);
	}
	
	
	public String getClientNameForOrderId (int orderId) {
		return template.queryForObject (
			"SELECT * from orderAndClient WHERE orderId = ?",
			(rs, rownum) -> {
				return rs.getString("clientName");
			},
			orderId
		);
	}
	
	
	public String getDateOfDeliveryForOrderId (int orderId) {
		return template.queryForObject (
			"SELECT * from calendarEvents WHERE orderId = ?",
			(rs, rownum) -> {
				return rs.getString("start");
			},
			orderId
		);
	}
	
	public String getProductNameForProductId (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {
				return rs.getString("name");
			},
			productId
		);
	}
	
	
	public List <OrderItemEntity> getOrderItemListForOrderId (int orderId) {
		return template.query (
			"SELECT * from orderDetailsForOrderId WHERE orderId = ?",
			(ps) -> {
				ps.setInt (1, orderId);
			},
			(rs, rownum) -> {
				String productId = rs.getString ("productId");
				String productName = getProductNameForProductId (productId);
				return new OrderItemEntity (
					productId,
					productName,
					rs.getInt ("productQuantity")
				);
			}
		);
	}
	
	
	@Override
	public  OrderDetailsEntityForView getOrderDetailsForOrderId (int orderId) {
		
		OrderDetailsEntityForView ob = new OrderDetailsEntityForView();
		
		ob.setOrderId (orderId);
		ob.setDateOfDelivery (getDateOfDeliveryForOrderId (orderId));
		String clientName = getClientNameForOrderId (orderId);
		ob.setClientName (clientName);
		ob.setClientType (getClientTypeForClientName (clientName));
		ob.setClientTransport (getClientTransportForClientName (clientName));
		ob.setClientPhone (getClientContactForClientName (clientName));
		ob.setClientEmail (getClientEmailForClientName (clientName));
		ob.setClientAddress (getClientAddressForClientName (clientName));
		
		List<OrderItemEntity> items;
		items = getOrderItemListForOrderId (orderId);
		
		OrderItemEntity items2 [] = new OrderItemEntity [55];
		
		int idx = 0;
		System.out.println ("Size of items = " + items.size());
		for (OrderItemEntity orderItemEntity : items) {
			System.out.println ("idx = " + idx);
			System.out.println ("productId = " + orderItemEntity.getProductId());
//			items2 [idx].setProductId (orderItemEntity.getProductId());
//			items2 [idx].setProductName (orderItemEntity.getProductName());
//			items2 [idx].setProductQuantity(orderItemEntity.getProductQuantity());
			++idx;
		}
		
		ob.setItems(items2);
		return ob;
		
	}
}



























