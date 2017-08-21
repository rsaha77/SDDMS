package project.controller;
//import static java.lang.System.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.dao.spec.OrderDao;
import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.MultipleOrdersEntity;
import project.entity.OrderDetailsCreationEntity;
import project.entity.OrderDetailsEntity;
import project.entity.OrderDetailsEntityForView;
import project.entity.OrderDetailsFetchEntity;
import project.entity.OrderDetailsListEntity;
import project.entity.OrderHistoryFetchEntity;
import project.entity.OrderHistoryListEntity;
import project.entity.OrderProcessingCreationEntity;
import project.entity.ReportProductShortageInfoEntity;
import project.service.spec.OrderService;

@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDao orderDao;
	
	@RequestMapping (value = "/orders")
	public String orders() {
		return "orders";
	}
	
	@RequestMapping (value = "/order/fetchOrderTable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public OrderDetailsListEntity fetchOrderTable (@RequestBody OrderDetailsFetchEntity entity) {
		return  orderService.getOrderBy (entity);
	}
	
	@RequestMapping (value = "/order/placeOrder", method = RequestMethod.POST)
	@ResponseBody
	public void orderProduct (@RequestBody OrderDetailsCreationEntity orderDetailsCreationEntity) {
		orderService.placeOrder (orderDetailsCreationEntity);
	}
	
	
	@RequestMapping (value = "/order/placeOrderMultiple", method = RequestMethod.POST)
	@ResponseBody
	public void orderProductMultiple (@RequestBody MultipleOrdersEntity multipleOrdersEntity) {
		System.out.println (multipleOrdersEntity);
		orderDao.placeOrderMultiple (multipleOrdersEntity);
	}
	
	@RequestMapping (value = "/order/orderProcess", method = RequestMethod.POST)
	@ResponseBody
	public void orderProcess (@RequestBody OrderProcessingCreationEntity orderProcessingCreationEntity) {
		orderService.processOrder (orderProcessingCreationEntity);
	}
	
	@RequestMapping (value = "/order/deleteOrder", method = RequestMethod.POST)
	@ResponseBody
	public void deleteOrder (@RequestBody OrderProcessingCreationEntity orderProcessingCreationEntity) {
		orderService.deleteBy (orderProcessingCreationEntity);
	}
	
	@RequestMapping (value = "/order/fetchOrderHistoryTable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public OrderHistoryListEntity fetchOrderHistoryTable (@RequestBody OrderHistoryFetchEntity entity) {
		return orderService.getOrderHistoryBy (entity);
	}
	
	@RequestMapping (value = "/order/modifyOrderQuantity", method = RequestMethod.POST)
	@ResponseBody
	public void modifyOrderQuantity (@RequestBody ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) {
		orderService.modifyOrderQuantity (modifyOrderQuantityCreationEntity);
	}
	
	@RequestMapping (value = "/order/reportProductShortageInfo", method = RequestMethod.POST)
	@ResponseBody
	public void reportProductShortageInfo (@RequestBody ReportProductShortageInfoEntity reportProductShortageInfoEntity) {
		orderService.reportProductShortageInfo (reportProductShortageInfoEntity);
	}
	
	@RequestMapping (value = "/order/getOrderDetailsForOrderId", method = RequestMethod.POST)
	@ResponseBody
	public OrderDetailsEntityForView getOrderDetailsForOrderId (@RequestParam int orderId) {
		return orderDao.getOrderDetailsForOrderId (orderId);
	}
}
















