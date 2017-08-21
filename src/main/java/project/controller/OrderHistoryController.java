package project.controller;

//import static java.lang.System.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.service.spec.OrderHistoryService;

@Controller
public class OrderHistoryController {
	
	@Autowired
	OrderHistoryService orderHistoryService;
	
	@RequestMapping (value = "/orderHistory")
	public String orderHistory () {
		return "orderHistory";
	}
	
	@RequestMapping (value = "/orderHistory/deleteOrderHistory", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteOrderHistory (@RequestParam String orderId) {
		orderHistoryService.deleteBy (orderId);
	}
	
}
