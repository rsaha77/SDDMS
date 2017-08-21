package project.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import static java.lang.System.out;












import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;










import project.dao.spec.ProductDao;
import project.dto.EventADto;
import project.entity.AddProductQuantityByManagerEntity;
import project.entity.ClientDetailsFetchEntity;
import project.entity.EventAEntity;
import project.entity.EventEntity;
import project.entity.EventFetchEntity;
import project.entity.InStockEntity;
import project.entity.InStockListEntity;
import project.entity.ProdAvailEntity;
import project.entity.ProdAvailListEntity;
import project.entity.ProductRequestEntity;
import project.entity.ProductRequestListEntity;
import project.entity.ProductRequestedEntity;
import project.entity.ProductShortageEntityForManager;
import project.entity.ProductShortageFetchEntity;
import project.entity.ProductShortageListEntityForManager;
import project.service.spec.ProductService;

@Controller
public class DashboardManagerController {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private ProductDao productDao;

	@RequestMapping(value = "/indexM")
	public String dashboardManager () {
		return "dashboardManager";
	}

	@RequestMapping(value = "/calendarManager")
	public String calendarManager () {
		return "calendarManager";
	}

	@Autowired
	private ProductService productService;


	public String getInstance () {
		DateFormat df = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();   
		return df.format(today);
	}

	@RequestMapping (value = "/indexM/fetchProductShortage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductShortageListEntityForManager getProductShortageEntityListForManager (@RequestBody ProductShortageFetchEntity productShortageFetchEntity) {
		List <ProductShortageEntityForManager> productShortageEntityForManager = productService.getProductShortageEntityListForManager ();
		return new ProductShortageListEntityForManager (0, 10, 10, productShortageEntityForManager);
	}



	@RequestMapping (value = "/indexM/fetchProductRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductRequestListEntity fetchProductRequestTable (@RequestBody ProductShortageFetchEntity entity) {
		List <ProductRequestEntity> productRequestEntityForManager = productDao.fetchProductRequestTable ();
		return new ProductRequestListEntity (0, 10, 10, productRequestEntityForManager);
	}


	@RequestMapping (value = "/dashboardManager/addProductQuantity", method = RequestMethod.POST)
	@ResponseBody
	public void addProductQuantity (@RequestBody  AddProductQuantityByManagerEntity addProductQuantityByManagerEntity) {
		productService.addProductQuantityByTheManager (addProductQuantityByManagerEntity);
	}


	public String getClientIdFromFromProductRequestTable (String productName) {
		return template.queryForObject (
				"SELECT * FROM productRequest WHERE productName = ?",
				(rs, rownum) -> {
					return rs.getString ("requestByClients");
				},
				productName
				);
	}


	private static final String INSERT_PRODUCT = "INSERT INTO PRODUCT "
			+ " (productId, name, price, type, available, ordered, remaining, dateOfAddition)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


	private static final String INSERT_AVAIL_NOW = "INSERT INTO productAvailableNow "
			+ " (productName, productId, clientId)"
			+ " VALUES (?, ?, ?)";

	@RequestMapping (value = "/indexM/addProductRequested", method = RequestMethod.POST)
	@ResponseBody
	public void addProductRequested (@RequestBody  ProductRequestedEntity productRequestedEntity) {


		// Insert Into productAvailableNow
		template.update (
				INSERT_AVAIL_NOW,
				(ps) -> {
					ps.setString (1, productRequestedEntity.getProductName2());
					ps.setString (2, productRequestedEntity.getProductId2());
					ps.setString (3, getClientIdFromFromProductRequestTable (productRequestedEntity.getProductName2()));
				}
				);


		// Insert into product table
		template.update (
				INSERT_PRODUCT,
				(ps) -> {
					ps.setString (1, productRequestedEntity.getProductId2());
					ps.setString (2, productRequestedEntity.getProductName2());
					ps.setString (3, productRequestedEntity.getProductPrice2());
					ps.setString (4, productRequestedEntity.getProductType2());
					ps.setString (5, productRequestedEntity.getProductQuantity2());
					ps.setString (6, "0");
					ps.setString (7, productRequestedEntity.getProductQuantity2());
					ps.setString (8, getInstance ());
				}
				);



		// Delete from productRequest table
		template.update (
				"DELETE from productRequest WHERE productName = ?",
				(ps) -> {
					ps.setString (1, productRequestedEntity.getProductName2());
				}
				);				
	}


	@RequestMapping (value = "/dashboardManager/getCountFromProductRequestTable", method = RequestMethod.POST)
	@ResponseBody
	public int getCountFromProductRequestTable () {
		return template.queryForObject (
				"SELECT count(*) from productRequest",
				(rs, rownum) -> {
					return rs.getInt (1);
				}
				);
	}


	@RequestMapping (value = "/dashboardManager/getCountFromProductShortageTable", method = RequestMethod.POST)
	@ResponseBody
	public int getCountFromProductShortageTable () {
		return template.queryForObject (
				"SELECT count(*) from productShortage",
				(rs, rownum) -> {
					return rs.getInt (1);
				}
				);
	}



	public List <ProdAvailEntity> getProdAvailList () {
		return template.query (
				"SELECT * from productAvailableNow",
				(rs, rownum) -> {
					return new ProdAvailEntity (
							rs.getString ("productId"),
							rs.getString ("productName"),
							rs.getString ("clientId")
							);
				}
				);
	}


	public List <InStockEntity> getInStockList () {
		return template.query (
				"SELECT * from inStock",
				(rs, rownum) -> {
					return new InStockEntity (
							rs.getString ("productId"),
							rs.getString ("productName"),
							rs.getString ("productType")
							);
				}
				);
	}


	public List <InStockEntity> getProdNew () {
		return template.query (
				"SELECT * from newProductsAdded",
				(rs, rownum) -> {
					return new InStockEntity (
							rs.getString ("productId"),
							rs.getString ("productName"),
							rs.getString ("productType")
							);
				}
				);
	}


	@RequestMapping (value = "/indexM/getProdAvail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProdAvailListEntity getProdAvailList (@RequestBody ClientDetailsFetchEntity entity) {
		List <ProdAvailEntity> prodAvailList = getProdAvailList ();
		return new ProdAvailListEntity(0, 10, 10, prodAvailList);
	}



	@RequestMapping (value = "/indexM/getInStock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public InStockListEntity getInStock (@RequestBody ClientDetailsFetchEntity entity) {
		List <InStockEntity> inStockList = getInStockList ();
		return new InStockListEntity (0, 10, 10, inStockList);
	}


	@RequestMapping (value = "/indexM/getProdNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public InStockListEntity getProdNew (@RequestBody ClientDetailsFetchEntity entity) {
		List <InStockEntity> inStockList = getProdNew ();
		return new InStockListEntity (0, 10, 10, inStockList);
	}





	@RequestMapping (value = "/indexM/getProdAvailCount", method = RequestMethod.POST)
	@ResponseBody
	public int getProdAvailCount () {
		return template.queryForObject (
				"SELECT count(*) from productAvailableNow",
				(rs, rownum) -> {
					return rs.getInt (1);
				}
				);
	}


	@RequestMapping (value = "/indexM/getInStockCount", method = RequestMethod.POST)
	@ResponseBody
	public int getInStockCount () {
		return template.queryForObject (
				"SELECT count(*) from inStock",
				(rs, rownum) -> {
					return rs.getInt (1);
				}
				);
	}


	@RequestMapping (value = "/indexM/getProdNewCount", method = RequestMethod.POST)
	@ResponseBody
	public int getNewProdAddCount () {
		return template.queryForObject (
				"SELECT count(*) from newproductsadded",
				(rs, rownum) -> {
					return rs.getInt (1);
				}
				);
	}


	@RequestMapping(value = "getTarget", method = RequestMethod.POST)
	@ResponseBody
	public double getTarget() {
		return template.queryForObject (
				"SELECT dect FROM DUMMY WHERE id=1",
				(rs, rownum) -> {
					return rs.getDouble (1);
				}
				);
	}

	@RequestMapping(value = "updateTarget", method = RequestMethod.POST)
	@ResponseBody
	public void updateTarget(@RequestParam Double target) {
		System.out.println(target);
		//		netSalesService.updateTarget(store_id, start, end, target);
		template.update("UPDATE DUMMY SET dect = ? WHERE id = 1", (ps) -> {
			ps.setDouble(1, target);
		});
	}

	@RequestMapping(value = "updateSRTarget", method = RequestMethod.POST)
	@ResponseBody
	public void updateSRTarget(@RequestParam Double v1, @RequestParam Double v2, @RequestParam Double v3) {
		//System.out.println(v1+ " "+v2+" "+v3);
		//netSalesService.updateTarget(store_id, start, end, target);
		for(int i=1; i<=3; i++) {
			final int ti = i;
			template.update("UPDATE DUMMY SET dect = ? WHERE id = ?", (ps) -> {
				if(ti==1) ps.setDouble(1, v1);
				if(ti==2) ps.setDouble(1, v2);
				if(ti==3) ps.setDouble(1, v3);

				ps.setInt(2, ti+1);
			});
		}
	}

	@RequestMapping(value = "getEvents", method = RequestMethod.POST)
	@ResponseBody
	public EventEntity getEvents(@RequestParam String uname, Principal principal) {
		if(uname.compareTo("0") == 0) uname = principal.getName();
		//return storeProductService.getEvents(uname);
		String funame = uname;

		List<EventFetchEntity> list = template.query("SELECT * FROM EVENT WHERE user_account_id = ?", ps -> {
			ps.setString(1, funame);
		}, (rs, rownum) -> {
			return new EventFetchEntity(rs.getInt("id"), rs.getString("user_account_id"), rs.getString("title"), 
					rs.getString("type"), rs.getString("dt1")
					, rs.getString("dt2"), rs.getString("location"), rs.getString("byl"), rs.getInt("eid"));
		});

		EventEntity re = new EventEntity();
		re.setList(list);


		return re;
	}

	@RequestMapping(value = "eventAList", method = RequestMethod.POST)
	@ResponseBody
	public EventAEntity eventAList(@RequestParam int id) {
		//return storeProductService.eventAList(id);

		List<EventADto> list = template.query("SELECT ua.firstname, ur.role FROM USER_ACCOUNT ua, event e, USER_ROLE ur WHERE "+
				"e.eid=? AND e.user_account_id=ua.id AND ur.user_account_id=e.user_account_id AND ur.role != 'MANAGER' ORDER BY ur.role", ps -> {
					ps.setInt(1, id);
				}, (rs, rownum) -> {
					return new EventADto(rs.getString("firstname"), rs.getString("role"));
				});

		EventAEntity re = new EventAEntity();

		re.setList(list);
		return re;
	}
	
	@RequestMapping(value = "getEID", method = RequestMethod.POST)
	@ResponseBody
	public int getEID(Principal principal) {
		//return storeProductService.getEID(storeService.getStoreId(principal.getName()));
		
		int c = template.queryForObject("SELECT COUNT(*) AS c FROM event", (rs, rownum) -> {
			return rs.getInt("c");
		});
		if(c == 0) return 1;
		
		c = template.queryForObject("SELECT MAX(eid) AS c FROM event", (rs, rownum) -> {
			return rs.getInt("c");
		});
		
		return c+1;
	}
	
	@RequestMapping(value = "addEvent", method = RequestMethod.POST)
	@ResponseBody
	public void addEvent(@RequestBody EventFetchEntity entity) {
		//storeProductService.addEvent(entity);
		List<String> list = new ArrayList<String>();
		if(entity.getByl().compareTo("SALES MANAGER") == 0) {
			
			if(entity.getUid().compareTo("1") == 0)  { // WHOLE STAFF
				list.add("salesman");
				list.add("salesman2");
				list.add("salesman3");	
			}
			else {
				list.add(entity.getUid());
			}			
		}
		
		for(String o: list) {
			template.update("INSERT INTO EVENT (user_account_id, title, type, dt1, dt2, location, byl, eid) VALUES (?,?,?,?,?,?,?,?)", (ps) -> {
				ps.setString(1, o);
				ps.setString(2, entity.getTitle());
				ps.setString(3, entity.getType());
				ps.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getDt1()));
				ps.setTimestamp(5, java.sql.Timestamp.valueOf(entity.getDt2()));
				ps.setString(6, entity.getLocation());
				ps.setString(7,  entity.getByl());
				ps.setInt(8, entity.getEid());
			});
			}
		
		System.out.println("event added successfulyy");
	}
}



















