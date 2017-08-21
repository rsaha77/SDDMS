package project.dao.impl;

import java.io.IOException;

import static java.lang.System.out;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dto.ProductDetailsDto;
import project.dao.spec.ProductDao;
import project.dao.spec.RecentActivityDao;
import project.entity.AddProductQuantityByManagerEntity;
import project.entity.ChartEntity;
import project.entity.EmergencyReplacementFetchEntity;
import project.entity.EmergencyReplacementProductListEntity;
import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.OrderDetailsCreationEntity;
import project.entity.OrderProcessingCreationEntity;
import project.entity.ProductDetails;
import project.entity.ProductDetailsEntity;
import project.entity.ProductRequestEntity;
import project.entity.ProductShortageEntityForManager;
import project.entity.ReportProductShortageInfoEntity;
import project.entity.RequestForProductEntity;
import project.exception.ServiceException;

@Repository
public class ProductDaoImpl implements ProductDao {
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	RecentActivityDao recentActivityDao;
	
	private static final String FETCH = "SELECT * FROM product";
	
	private static final String PRODUCT_COUNT = "SELECT COUNT(*) FROM product";
	
	private static final String DELETE_PRODUCT = "DELETE FROM PRODUCT WHERE productId = ?";
	
	
	public String getInstance () {
		DateFormat df = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();   
		return df.format(today);
	}
	
	
	public String getProductNameFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("name");},
			productId
		);
	}
	
	public int clientCountInClientTable (String clientId) {
		return template.queryForObject (
			"Select count(*) from client where id like ?",
			(rs, rownum) -> {
				return rs.getInt(1);
			},
			clientId
		);
	}
	
	
	public boolean clientIdExistsInClientTable (String clientId) {
		int temp = clientCountInClientTable (clientId);
//		System.out.println ("temp : " + temp);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getClientTypeFromClientTableForClientId (String clientId) {
		return template.queryForObject (
			"Select * from client where id like ?",
			(rs, rownum) -> {
				return rs.getString ("type");
			},
			clientId
		);
	}
	
	
	
	public String getLeadTypeFromLeadTableForClientId (String clientId) {
		return template.queryForObject (
			"Select leadType from lead where clientId = ?",
			(rs, rownum) -> {
				return rs.getString ("leadType");
			},
			clientId
		);
	}
	
	public String getAvailableFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("available");},
			productId
		);
	}
	
	public String getOrderedFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("ordered");},
			productId
		);
	}
	
	public String getRemainingFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("remaining");},
			productId
		);
	}
	
	public String getTypeFromProductTable (String productId) {
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {return rs.getString ("type");},
			productId
		);
	}
	
	public int convertStringToInt (String s) {
		if (s.length () == 0) {
			return 0;
		}
		boolean negative = false;
		int i = 0;
		if (s.charAt (0) == '-') {
			negative = true;
			i = 1;
		}
		int ret = 0;
		for (;i < s.length (); ++i) {
			ret = ret * 10 + Character.getNumericValue (s.charAt (i));
		}
		if (negative == true) {
			ret = -ret;
		}
		return ret;
	}
	
	public String convertIntToString (int a) {
		return Integer.toString (a);
	}
	
	
	public String getLabelledSeason (String s) {
		String ret;
		if (s.equals("Summer")) {
			ret = "<span class='label label-warning'>" + s + "</span>";
		} else if (s.equals("Winter")) {
			ret = "<span class='label label-info'>" + s + "</span>";
		} else {
			ret = "<span class='label label-success'>" + s + "</span>";
		}
		return ret;
	}
	
	
	@Override
	public List <ProductDetailsDto> getBy (String clientIdForReco, int size, int start) throws IOException {
//		System.out.println ("=======> one");
		String typeOfClient_ = "";
		
		if (clientIdForReco.compareTo ("ZZZZ") != 0) {
			if (clientIdExistsInClientTable (clientIdForReco) == true) {
				typeOfClient_ = getClientTypeFromClientTableForClientId (clientIdForReco);
			} else {
				typeOfClient_ = getLeadTypeFromLeadTableForClientId (clientIdForReco);
			}
		}
		
		String typeOfClient = typeOfClient_;

		if (clientIdForReco.compareTo("ZZZZ") == 0 || typeOfClient.compareTo ("All") == 0) {
//			System.out.println ("=======> if");
			try {
				return template.query (
					FETCH,
					ps -> {
					},
					(rs, rownum) -> {
						String remaining = rs.getString ("remaining");
						if (remaining.charAt (0) == '-') {
							String newRemaining = "<span class='label label-danger'> <font size = '2.9'>" + remaining + " </font> </span>";
							remaining = newRemaining;
						}
						
						String isNew = "";
						String getDate = rs.getString ("dateOfAddition");
						int a = (int) getDate.charAt (3) - 48;
						int b = (int) getDate.charAt (4) - 48;
						int c = a * 10 + b;
						
						String todaysDate = getInstance ();
						int p = (int) todaysDate.charAt (3) - 48;
						int q = (int) todaysDate.charAt (4) - 48;
						int r = p*10 + q;
						
//						System.out.println ("=======> " + getDate + " " + a + " " + b + " " + c + " " + r);
						
						if (c == r) {
							isNew = "<span class='label label-success'>" + "NEW" + "</span>";
						}
						
//						System.out.println ("isNew = " + isNew);
						
						String labelledSeason = getLabelledSeason (rs.getString("season"));
						
						return new ProductDetailsDto (
							rs.getString ("productId"),
							rs.getString ("name"),
							rs.getString ("price"),
							rs.getString ("type"),
							labelledSeason,
							isNew,
							rs.getString ("available"),
							rs.getString ("ordered"),
							remaining
						);
					}
				);
			} catch (DataAccessException e) {
				throw new IOException (e);
			}
		} else {
//			System.out.println ("=======> else");
			try {
				return template.query (
					"Select * from product where type like ? order by quantitySold desc",
					ps -> {
						ps.setString (1, "%" + typeOfClient + "%");
					},
					(rs, rownum) -> {
						String remaining = rs.getString ("remaining");
						if (remaining.charAt (0) == '-') {
							String newRemaining = "<span class='label label-danger'> <font size = '2.9'>" + remaining + " </font> </span>";
							remaining = newRemaining;
						}
						
						String isNew = "";
						String getDate = rs.getString ("dateOfAddition");
						int a = (int) getDate.charAt (3) - 48;
						int b = (int) getDate.charAt (4) - 48;
						int c = a * 10 + b;
						
						String todaysDate = getInstance ();
						int p = (int) todaysDate.charAt (3) - 48;
						int q = (int) todaysDate.charAt (4) - 48;
						int r = p*10 + q;

						if (c == r) {
							isNew = "<span class='label label-success'>" + "NEW" + "</span>";
						}
						
						String labelledSeason = getLabelledSeason (rs.getString("season"));
						
						return new ProductDetailsDto (
							rs.getString ("productId"),
							rs.getString ("name"),
							rs.getString ("price"),
							rs.getString ("type"),
							labelledSeason,
							isNew,
							rs.getString ("available"),
							rs.getString ("ordered"),
							remaining
						);
					}
				);
			} catch (DataAccessException e) {
				throw new IOException (e);
			}
		}
		
	}
	

	@Override
	public int getTotalCount () throws IOException {
		try {
			return template.queryForObject (PRODUCT_COUNT,
					(rs, rownum) -> {
						return rs.getInt(1);
					}
				);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	private static final String INSERT_PRODUCT = "INSERT INTO PRODUCT "
			+ " (productId, name, price, available, remaining, type, dateOfAddition)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_NEW_PROD = "INSERT INTO newProductsAdded "
			+ " (productId, productName, productType)"
			+ " VALUES (?, ?, ?)";
	
	
	@Override
	public void insert (ProductDetailsDto productDetails) throws ServiceException, IOException {
		
		// Add it in product table
		try {
			template.update (
				INSERT_PRODUCT,
				(ps) -> {
					ps.setString (1, productDetails.getProductId ());
					ps.setString (2, productDetails.getName ());
					ps.setString (3, productDetails.getPrice ());
					ps.setString (4, productDetails.getAvailable ());
					ps.setString (5, productDetails.getAvailable ()); // Initially, remaining is same as available
					ps.setString (6, productDetails.getType ());
					ps.setString (7, getInstance ());
				}
			);
		} catch  (DataAccessException e) {
			throw new IOException (e);
		}
		
		
		
		// Add it to new products added
		template.update (
			INSERT_NEW_PROD,
			(ps) -> {
				ps.setString(1, productDetails.getProductId ());
				ps.setString(2, productDetails.getName ());
				ps.setString(3, productDetails.getType ());
			}
		);
		
		recentActivityDao.insert ("Added", " a new product (" + productDetails.getName () + ", " + productDetails.getProductId () + ")", getInstance ());
	}
	
	
	
	@Override
	public void update (ProductDetailsDto productDetails) throws IOException {
		try {
			template.update (
				"UPDATE PRODUCT SET name = ?, price = ?, available = ?, remaining = ?, type = ? WHERE productId = ?", 
				(ps) -> {
					ps.setString (1, productDetails.getName ());
					ps.setString (2, productDetails.getPrice ());
					ps.setString (3, productDetails.getAvailable ());
					ps.setString (4, convertIntToString (convertStringToInt (productDetails.getAvailable ()) - convertStringToInt (getOrderedFromProductTable (productDetails.getProductId ()))));
					ps.setString (5, productDetails.getType ());
					ps.setString (6, productDetails.getProductId ());
				}
			);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		recentActivityDao.insert ("Updated", " the product (" + productDetails.getName () + ", " + productDetails.getProductId () + ")", getInstance ());
	}
	
	
	@Override
	public void deleteBy (String productId) throws IOException {
		String productName = getProductNameFromProductTable (productId);
		try {
			template.update (DELETE_PRODUCT, (ps) -> ps.setString(1, productId));
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		recentActivityDao.insert ("Deleted", " the product (" + productName + ", " + productId + ")", getInstance ());
	}
	
	
	@Override
	public boolean updateProductQuantityAfterPlacingOrder (String productId, String orderQuantityString) throws IOException {
		int available = convertStringToInt (getAvailableFromProductTable (productId));
		int orderedNew = convertStringToInt (getOrderedFromProductTable (productId)) +  convertStringToInt (orderQuantityString);
		out.println ("Avail : " + getAvailableFromProductTable (productId) + " || ord : " + getOrderedFromProductTable (productId) + "\n");
		out.println ("OQ_String : " + orderQuantityString + " || OQ_INT : " + convertStringToInt (orderQuantityString)   +" || OrdNew : " + orderedNew + "\n");
		String remaining = convertIntToString (available - orderedNew);
		boolean outOfStockStatus = false;
		if (remaining.charAt (0) == '-') {
			outOfStockStatus = true;
		}
		try {
			template.update (
				"UPDATE product SET ordered = ?, remaining = ?  WHERE productId = ?",
				ps -> {
					ps.setString (1, convertIntToString (orderedNew));
					ps.setString (2, remaining);
					ps.setString (3, productId);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		return outOfStockStatus;
	}
	
	@Override
	public void updateProductQuantityAfterProcessingOrder (String productId, String orderQuantity) throws IOException {
		String availableNew = convertIntToString (convertStringToInt (getAvailableFromProductTable (productId)) - convertStringToInt (orderQuantity));
		String orderedNew = convertIntToString (convertStringToInt (getOrderedFromProductTable (productId)) - convertStringToInt (orderQuantity));
		try {
			template.update (
				"UPDATE product SET available = ?, ordered = ? WHERE productId = ?",
				ps -> {
					ps.setString (1, availableNew);
					ps.setString (2, orderedNew);
					ps.setString (3, productId);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		recentActivityDao.insert ("Updated", " product quantity of the product(" + getProductNameFromProductTable (productId) + ", " + productId + ")", getInstance ());	
	}
	
	@Override
	public void updateProductCountAfterDeletingOrder (OrderProcessingCreationEntity orderProcessingCreationEntity) throws IOException {
		String productId = orderProcessingCreationEntity.getProductId ();
		String ordered = convertIntToString (convertStringToInt (getOrderedFromProductTable (productId)) - convertStringToInt (orderProcessingCreationEntity.getOrderQuantity()));
		String remaining = convertIntToString (convertStringToInt (getRemainingFromProductTable (productId)) + convertStringToInt (orderProcessingCreationEntity.getOrderQuantity ()));
		try {
			template.update (
				"UPDATE product SET ordered = ?, remaining = ?  WHERE productId = ?",
				ps -> {
					ps.setString (1, ordered);
					ps.setString (2, remaining) ;
					ps.setString (3, productId);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		recentActivityDao.insert ("Updated", " product quantity of the product(" + getProductNameFromProductTable (productId) + ", " + productId + ")", getInstance ());
	}
	
	@Override
	public void updateProductCountAfterReceivingRequestFromSalesperson (ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) throws IOException {
		String productId = modifyOrderQuantityCreationEntity.getProductId ();
		int old = convertStringToInt (modifyOrderQuantityCreationEntity.getOrderQuantityOld ());
		int new_ = convertStringToInt (modifyOrderQuantityCreationEntity.getOrderQuantityNew ());
		String ordered = convertIntToString (convertStringToInt (getOrderedFromProductTable (productId)) - old + new_);
		String remaining = convertIntToString (convertStringToInt (getRemainingFromProductTable (productId)) + old - new_);
		try {
			template.update (
				"UPDATE product SET ordered = ?, remaining = ?  WHERE productId = ?",
				ps -> {
					ps.setString (1, ordered);
					ps.setString (2, remaining);
					ps.setString (3, productId);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		recentActivityDao.insert ("Updated", " product quantity of the product(" + getProductNameFromProductTable (productId) + ", " + productId + ") after receiveing request from sales manager", getInstance ());
	}
	
	public int countFromClientTable (String clientId) {
		return template.queryForObject (
			"SELECT count(*) from client where id = ?",
			(rs, rownum) -> {
				return rs.getInt(1);
			},
			clientId
		);
	}
	
	public boolean isClient (String clientId) {
		if (countFromClientTable (clientId) == 1) {
			return true;
		}
		return false;
	}

	
	public String GetClientTypeFromClientTable (String clientId) {
		return template.queryForObject (
			"SELECT * from client where id = ?",
			(rs, rownum) -> {return rs.getString ("type");},
			clientId
		);
	}
	
	
	public String GetLeadTypeFromLeadTable (String clientId) {
		return template.queryForObject (
			"SELECT * from lead where clientId = ?",
			(rs, rownum) -> {return rs.getString ("leadType");},
			clientId
		);
	}
	
	@Override
	public List <EmergencyReplacementProductListEntity> getEmergencyReplacementProductListForProductId (EmergencyReplacementFetchEntity emergencyReplacementFetchEntity) throws IOException {
		String clientType = "";
		String clientId = emergencyReplacementFetchEntity.getClientId ();
		if (isClient (clientId)) {
			clientType = GetClientTypeFromClientTable (clientId);
		} else {
			clientType = GetLeadTypeFromLeadTable (clientId);
		}
		String productId = emergencyReplacementFetchEntity.getProductId ();
		String productName = getProductNameFromProductTable (productId);
		recentActivityDao.insert ("Emergency Replacement", " for product(" + productName + ", " + productId + ")", getInstance ());
		if (clientType.compareTo ("All") == 0) {
			return template.query (
				"Select * from PRODUCT where productId not like ? and remaining not like ?",
				(ps) -> {
					ps.setString (1, emergencyReplacementFetchEntity.getProductId ());
					ps.setString (2, "-%");
				},
				(rs, rownum) -> {
					return new EmergencyReplacementProductListEntity (
						rs.getString ("productId"),
						rs.getString ("name"),
						rs.getString ("price")
					);
				}
			);
		}
		String clientType_ = clientType;
		return template.query (
			"Select * from PRODUCT where type like ? and productId not like ? and remaining not like ?",
			(ps) -> {
				ps.setString (1, "%" + clientType_ + "%");
				ps.setString (2, emergencyReplacementFetchEntity.getProductId ());
				ps.setString (3, "-%");
			},
			(rs, rownum) -> {
				return new EmergencyReplacementProductListEntity (
					rs.getString ("productId"),
					rs.getString ("name"),
					rs.getString ("price")
				);
			}
		);
	}
	
	@Override
	public List <ProductDetails> getProductIdAndProductName () throws IOException {
		try {
			return template.query (
				"Select * from product",
				ps -> {
				},
				(rs, rownum) -> {
					return new ProductDetails (
						rs.getString("productId"),
						rs.getString("name")
					);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	public int getCountInProductShortageTableForProductId (String productId) {
		return template.queryForObject (
			"Select count(*) from productShortage where productId = ?",
			(rs, rownum) -> {
				return rs.getInt(1);
			},
			productId
		);
	}
	
	public boolean ProductShortageExistsForProductId (String productId) {
		boolean productShortageExistsForProductId = false;
		if ((getCountInProductShortageTableForProductId (productId)) > 0) {
			productShortageExistsForProductId = true;
		}
		return productShortageExistsForProductId;
	}
	
	public String GetNearestDateOfDeliveryFromProductShortageTable (String productId) {
		String nearestDateOfDeliveryFromProductShortageTable = "9999-99-99";
		if (ProductShortageExistsForProductId (productId)) {
			nearestDateOfDeliveryFromProductShortageTable = template.queryForObject (
				"SELECT * from productShortage where productId = ?",
				(rs, rownum) -> {return rs.getString ("nearestDateOfDelivery");},
				productId
			);
		}
		return nearestDateOfDeliveryFromProductShortageTable;
	}
	
	public String min (String str1, String str2) {
		if (str1.compareTo (str2) <= 0) {
			return str1;
		} else {
			return str2;
		}
	}
	
	
	
	@Override
	public void updateProductShortageNoteToTheManagerAfterTheSalesmanReportsItToTheManager (ReportProductShortageInfoEntity  reportProductShortageInfoEntity) throws IOException {
		try {
			/* If the salesman is reporting out of stock at this moment,  it's assured that the product exists in the product shortage table 
			 * and it will definitely update the table. Damn! Such a long comment... Sigh!
			 */
			template.update (
				"UPDATE productShortage SET noteToTheManager = ? WHERE productId = ?",
				(ps) -> {
					ps.setString (1, reportProductShortageInfoEntity.getNoteToTheManager ());
					ps.setString (2, reportProductShortageInfoEntity.getProductId ());
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	
	public boolean productExistsInProductShortageTableForProductId (String productId) {
		boolean productExistStatus = true;
		if (getCountInProductShortageTableForProductId (productId) == 0) {
			productExistStatus = false;
		}
		return productExistStatus;
	}
	
	public static final String INSERT_PRODUCT_SHORTAGE = "Insert into productShortage"
			+ " (productId, productName, nearestDateOfDelivery, clientsAffected, status)"
			+ " VALUES (?, ?, ?, ?, ?)";
	
	@Override
	public void reportProductShortageAfterPlacingOrder (OrderDetailsCreationEntity orderDetailsCreationEntity) throws IOException {
		String productId = orderDetailsCreationEntity.getProId ();
		String productName = getProductNameFromProductTable (productId);
		String currentNearestDate = GetNearestDateOfDeliveryFromProductShortageTable (productId);
		String newNearestDate = min (currentNearestDate, orderDetailsCreationEntity.getOrderDate ());
		recentActivityDao.insert ("Product Shortage", " reported for product(" + productName + ", " + productId + ")", getInstance ());
		String affectedClientId = orderDetailsCreationEntity.getClientName ();
		
		if (productExistsInProductShortageTableForProductId (productId) == false) {
			// Add Info to Product Shortage Table
			try {
				template.update (
					INSERT_PRODUCT_SHORTAGE,
					(ps) -> {
						ps.setString (1, productId);
						ps.setString (2, orderDetailsCreationEntity.getProductName ());
						ps.setString (3, newNearestDate); // best one
						ps.setString (4, orderDetailsCreationEntity.getClientName ()); // best one
						ps.setString (5, "URGENT"); // best one
					}
				);
			} catch (DataAccessException e) {
				throw new IOException (e);
			}

			// clientsAffected (new entry)
			try {
				template.update (
					"UPDATE productShortage SET clientsAffected = ? WHERE productId like ?",
					ps -> {
						ps.setString (1, affectedClientId + " ");
						ps.setString (2, productId);
					}
				);
			} catch (DataAccessException e) {
				throw new IOException (e);
			}
		} else {
			
			// clientsAffected (add it to the existing row)
			try {
				template.update (
					"UPDATE productShortage SET clientsAffected = concat (?, clientsAffected) WHERE productId like ? and clientsAffected not like ? ",
					ps -> {
						ps.setString (1, affectedClientId + " ");
						ps.setString (2, productId);
						ps.setString (3, "");
					}
				);
			} catch (DataAccessException e) {
				throw new IOException (e);
			}
		}
		recentActivityDao.insert ("Reported", "out of stock for the product (" + orderDetailsCreationEntity.getProductName () + ", " + orderDetailsCreationEntity.getProId () + ") ordered by the client (" + orderDetailsCreationEntity.getClientName () + ", " + orderDetailsCreationEntity.getClientId () + ")", getInstance ());

	}
	
	public String getLabelType (String status) {
		String label = "info";
		if (status.equals ("Cancelled") || status.equals ("URGENT")) {
			label = "danger";
		} else if (status.equals ("Processed") || status.equals ("Placed")) {
			label = "success";
		} else if (status.equals ("Deleted") || status.equals("Updated")) {
			label = "warning";
		}
		return label;
	}

	public String getLabelledStatus (String status) {
		String front = "<span class='label label-";
		String labelType = getLabelType (status);
		String mid = "'>";
		String end = "</span>";
		return front + labelType + mid + status + end;
	}
	
	@Override
	public List <ProductShortageEntityForManager> getProductShortageEntityListForManager () throws IOException {
		try {
			return template.query (
				"Select * from productShortage",
				ps -> {
					
				},
				(rs, rownum) -> {
					int shortageQuantity = -convertStringToInt (getRemainingFromProductTable (rs.getString("productId")));
					String status = getLabelledStatus (rs.getString ("status"));
					String comments = "";
					String clientAffectedId = "";
					return new ProductShortageEntityForManager (
						rs.getString("productId"),
						rs.getString("productName"),
						shortageQuantity,
						status,
						comments,
						rs.getString ("clientsAffected"),
						clientAffectedId,
						rs.getString ("nearestDateOfDelivery"),
						rs.getString ("noteToTheManager")
					);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	
	@Override
	public void actionsAfterProductQuantityUpdatedByManager (AddProductQuantityByManagerEntity addProductQuantityByManagerEntity) throws IOException {
		String productId = addProductQuantityByManagerEntity.getProductId ();
		int quantity = convertStringToInt (addProductQuantityByManagerEntity.getQuantity());
		out.println ("quantity : " + quantity);
		String availNew = convertIntToString (convertStringToInt (getAvailableFromProductTable(productId)) + quantity);
		String remainNew = convertIntToString (convertStringToInt (getRemainingFromProductTable (productId)) + quantity);
		
		// Update the quantities in product table
		try {
			template.update (
				"UPDATE product SET available = ?, remaining = ? WHERE productId = ?",
				(ps) -> {
					ps.setString (1, availNew);
					ps.setString (2, remainNew);
					ps.setString (3, productId);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		
		// If the product shortage is over, delete it from product shortage table & remove "OUT OF STOCK" from the order table
		if (getRemainingFromProductTable (productId).charAt (0) != '-') {
			template.update (
				"DELETE from productShortage WHERE productId = ?",
				(ps) -> {
					ps.setString (1, productId);
				}
			);
			
			template.update (
				"UPDATE orders SET status = ? WHERE productId = ?",
				(ps) -> {
					ps.setString (1, "AVAILABLE");
					ps.setString (2, productId);
				}
			);
		}
		
		
		final String INSERT_IN_STOCK = "INSERT INTO inStock "
				+ " (productId, productName, productType)"
				+ " VALUES (?, ?, ?)";
		
		// Add it to the inStock for notification to salesman.
		if (getRemainingFromProductTable (productId).charAt (0) != '-') {
			template.update (
				INSERT_IN_STOCK,
				(ps) -> {
					ps.setString (1, productId);
					ps.setString (2, addProductQuantityByManagerEntity.getProductName ());
					ps.setString (3, getTypeFromProductTable (productId));
				}
			);
		}
	}
	
	
	public String getProductIdFromOrderTableByOrderId (int orderId) {
		return template.queryForObject (
			"Select * from orders where orderId = ?",
			(rs, rownum) -> {
				return new String (rs.getString ("productId"));
			},
			orderId
		);
	}
	
	
	@Override
	public ProductDetailsEntity getProductDetailsForOrderId (int orderId) {
		out.println ("orderId: " + orderId + "\n");
		String productId = getProductIdFromOrderTableByOrderId (orderId);
		out.println ("productId: " + productId + "\n");
		return template.queryForObject (
			"SELECT * from product WHERE productId = ?",
			(rs, rownum) -> {
				return new ProductDetailsEntity (
					productId,
					rs.getString ("name"), 
					rs.getString  ("price"),
					rs.getString ("type"),
					rs.getString ("season"),
					"NEW",
					rs.getString ("available"),
					rs.getString ("ordered"),
					rs.getString ("remaining")
				);
			},
			productId
		);
	}
	
	
	public int countForProductNameInProductTable (String productName) {
		return template.queryForObject (
			"SELECT count(*) FROM productRequest WHERE productName = ?",
			(rs, rownum) -> {
				return rs.getInt (1);
			},
			productName
		);
	}
	
	public boolean productNameExistsInProductRequestTable (String productName) {
		if (countForProductNameInProductTable (productName) == 1) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean requestForProduct (RequestForProductEntity requestForProductEntity) {
		// Assuming requested by just one client
		
		String productName = requestForProductEntity.getProductName();
		
		if (productNameExistsInProductRequestTable (productName) == true) {
			return false;
		} 
		template.update (
			"INSERT INTO productRequest "
			+ " (productName, productCount, requestByClients)"
			+ " VALUES (?, ?, ?)",
			
			(ps) -> {
				ps.setString (1, productName);
				ps.setInt (2, requestForProductEntity.getProductCount());
				ps.setString (3, requestForProductEntity.getClientId());
			}
		);
		return true;
	}
	
	
	@Override
	public List <ProductRequestEntity> fetchProductRequestTable () {
		return template.query (
				"Select * from productRequest",
				ps -> {
					
				},
				(rs, rownum) -> {
					return new ProductRequestEntity (
						rs.getString("productName"),
						rs.getInt ("productCount")
					);
				}
			);
	}
	
	
	public int getQuantitySold (String type) {
		type = "%" + type + "%";
		return template.queryForObject (
			"SELECT SUM(quantitySold) FROM product WHERE type like ?",
			(rs, rownum) -> {
				return rs.getInt (1);
			},
			type
		);
	}
	
	
	@Override
	public ChartEntity getQuantitySoldForTypes () {
		ChartEntity chartEntity = new ChartEntity ();
		chartEntity.setFast(getQuantitySold ("Fast Food Restaurant"));
		chartEntity.setHealth(getQuantitySold ("Health Store"));
		chartEntity.setMovie(getQuantitySold ("Movie Theatre"));
		chartEntity.setSports(getQuantitySold ("Sports Centre"));
		chartEntity.setWork (getQuantitySold ("Workplace"));
		return chartEntity;
	}
}












































