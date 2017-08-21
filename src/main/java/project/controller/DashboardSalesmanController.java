package project.controller;

import java.util.List;

//import static java.lang.System.out;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import project.entity.RecentActivityEntity;
//import project.service.spec.RecentActivityService;



import project.entity.RecentActivityEntity;
import project.service.spec.RecentActivityService;

@Controller
public class DashboardSalesmanController {
	
	@Autowired
	RecentActivityService recentActivityService;
	
	@Autowired
	private JdbcTemplate template;
	
	@RequestMapping (value={"/", "salesS", "/indexS"})
	public String indexSalesperson () {
		return "dashboardSalesman";
	}
	
	@RequestMapping (value = "/indexS/recentActivity", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <RecentActivityEntity> getRecentActivityList () {
		List <RecentActivityEntity> recentActivityList = recentActivityService.getRecentActivityList ();
//		out.println (recentActivityList);
		return recentActivityList;
	}
	
	@RequestMapping(value = "/deleteFromProdAvail", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteProdAvail (@RequestParam String productId) {
		template.update (
			"DELETE from productAvailableNow WHERE productId = ?",
			(ps) -> {
				ps.setString (1, productId);
			}
		);
	}
	
	
	@RequestMapping(value = "/deleteFromInStock", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteFromInStock (@RequestParam String productId) {
		template.update (
			"DELETE from inStock WHERE productId = ?",
			(ps) -> {
				ps.setString (1, productId);
			}
		);
	}
	
	
	@RequestMapping(value = "/deleteFromNewProd", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteFromNewProd (@RequestParam String productId) {
		template.update (
			"DELETE from newProductsAdded WHERE productId = ?",
			(ps) -> {
				ps.setString (1, productId);
			}
		);
	}
}






















