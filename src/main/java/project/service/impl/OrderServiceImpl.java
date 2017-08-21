package project.service.impl;

import static java.lang.System.out;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import project.dao.spec.CalendarDao;
import project.dao.spec.OrderDao;
import project.dao.spec.OrderHistoryDao;
import project.dao.spec.ProductDao;
import project.dao.spec.RecentActivityDao;
import project.dto.OrderDetailsDto;
import project.dto.OrderHistoryDto;
import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.OrderDetailsCreationEntity;
import project.entity.OrderDetailsEntity;
import project.entity.OrderDetailsFetchEntity;
import project.entity.OrderDetailsListEntity;
import project.entity.OrderHistoryEntity;
import project.entity.OrderHistoryFetchEntity;
import project.entity.OrderHistoryListEntity;
import project.entity.OrderProcessingCreationEntity;
import project.entity.ReportProductShortageInfoEntity;
import project.exception.ServiceException;
import project.service.spec.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private OrderHistoryDao orderHistoryDao;
	
	@Autowired
	private RecentActivityDao recentActivityDao;
	
	@Autowired
	private CalendarDao calendarDao;
	
	public String getInstance () {
		DateFormat df = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();   
		return df.format(today);
	}
	
	@Override
	public OrderDetailsListEntity getOrderBy (OrderDetailsFetchEntity entity) throws ServiceException {
		List <OrderDetailsDto> details = null;
		int orderCount = 0;
		try {
			details = orderDao.getBy (entity.getDraw(), entity.getLength(), entity.getStart());
			orderCount = orderDao.getTotalCount (entity.getId ());
		} catch (IOException e) {
			throw new ServiceException ("Cannot find order for orderId: " + entity.getId(), e);
		}
		
		List <OrderDetailsEntity> entities = Lists.newArrayList ();
		
		for (OrderDetailsDto detail : details) {
			OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity (detail);
			entities.add (orderDetailsEntity);
		}
		return new OrderDetailsListEntity (entity.getDraw (), orderCount, orderCount, entities);
	}
	
	@Override
	public OrderHistoryListEntity getOrderHistoryBy (OrderHistoryFetchEntity entity) throws ServiceException {
		List <OrderHistoryDto> details = null;
		int orderHistoryCount = 0;
		try {
			details = orderHistoryDao.getBy (entity.getDraw(), entity.getLength(), entity.getStart());
			orderHistoryCount = orderHistoryDao.getTotalCount (entity.getId ());
		} catch (IOException e) {
			throw new ServiceException ("Cannot find order history for orderId: " + entity.getId(), e);
		}
		
		List <OrderHistoryEntity> entities = Lists.newArrayList ();
		
		for (OrderHistoryDto detail : details) {
			OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity (detail);
			entities.add (orderHistoryEntity);
		}
		return new OrderHistoryListEntity (entity.getDraw (), orderHistoryCount, orderHistoryCount, entities);
	}
	
	@Override
	public void deleteBy (int orderId) throws ServiceException {
		try {
			orderDao.deleteBy (orderId);
		} catch (IOException e) {
			System.out.println ("Cannot delete order for orderId : " + orderId);
		}
	}
	
	@Override
	@Transactional
	public void placeOrder (OrderDetailsCreationEntity orderDetailsCreationEntity) throws ServiceException {
		String productId = orderDetailsCreationEntity.getProId ();
		String clientId = orderDetailsCreationEntity.getClientId ();
		boolean outOfStockStatus;
		out.println ("CLIENT ID" + clientId + "\n");
		
		try {
			outOfStockStatus = productDao.updateProductQuantityAfterPlacingOrder (productId, orderDetailsCreationEntity.getOrderQuantity());
		} catch (IOException e) {
			throw new ServiceException ("Cannot updateProductQuantity for productId: " + productId + " for the table PRODUCT\n", e);
		}
		
		if (outOfStockStatus == true) {
			try {
				productDao.reportProductShortageAfterPlacingOrder (orderDetailsCreationEntity);
			} catch (IOException e) {
				throw new ServiceException ("Cannot updateProductQuantity for productId: " + productId + " for the table PRODUCT\n", e);
			}
		}
		
		OrderDetailsDto orderDetails = new OrderDetailsDto (orderDetailsCreationEntity);
		try {
			orderDao.placeOrder (orderDetails);
		} catch (IOException e) {
			throw new ServiceException ("Cannot placeOrder for productId: " + orderDetailsCreationEntity.getProId (), e);
		}
	}
	
	@Override
	@Transactional
	public void processOrder (OrderProcessingCreationEntity orderProcessingCreationEntity) {
		try {
			recentActivityDao.insert ("Processed", " the order (Order Id = " + orderProcessingCreationEntity.getOrderId () + ") for the client ("
				+ orderProcessingCreationEntity.getClientName () + ", " + orderProcessingCreationEntity.getClientId ()
				+ ")"
				, getInstance ()
			);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
		String productId = orderProcessingCreationEntity.getProductId ();
		try {
			productDao.updateProductQuantityAfterProcessingOrder (productId, orderProcessingCreationEntity.getOrderQuantity());
		} catch (IOException e) {
			throw new ServiceException ("Cannot updateProductQuantity for productId: " + productId + " for the table PRODUCT\n", e);
		}
		try {
			orderHistoryDao.insert (orderProcessingCreationEntity, "PROCESSED", true);
		} catch (IOException e) {
			throw new ServiceException ("Cannot insert order history for the order id : " + orderProcessingCreationEntity.getOrderId () ,e);
		}
		try {
			orderDao.deleteBy (orderProcessingCreationEntity.getOrderId());
		} catch (IOException e) {
			throw new ServiceException ("Cannot delete order for : " + orderProcessingCreationEntity.getOrderId () + " from the table PRODUCT\n",e);
		}
	}
	
	@Override
	@Transactional
	public void deleteBy (OrderProcessingCreationEntity orderProcessingCreationEntity) throws ServiceException {
//		String productId = orderProcessingCreationEntity.getProductId ();
//		String clientId = orderProcessingCreationEntity.getClientId ();
		String reason = orderProcessingCreationEntity.getReasonManual ();
		if (reason.equals ("others")) {
			reason = orderProcessingCreationEntity.getReasonOther ();
		}
		try {
			recentActivityDao.insert ("Cancelled", " the order (Order Id: " + orderProcessingCreationEntity.getOrderId () + ") reason being: " + reason, getInstance ());
		} catch (IOException e) {
			throw new ServiceException (e);
		}
//		try {
//			productDao.insertPotentialClient (productId, clientId);
//		} catch (IOException e) {
//			throw new ServiceException ("Cannot insertPotentialClient : " + clientId + " from the table PRODUCT\n", e);
//		}
		try {
			productDao.updateProductCountAfterDeletingOrder (orderProcessingCreationEntity);
		} catch (IOException e) {
			throw new ServiceException ("Cannot updateProductCountAfterDeletingOrder for orderId" + orderProcessingCreationEntity.getOrderId (), e);
		}
		try {
			orderHistoryDao.insert (orderProcessingCreationEntity, "CANCELLED", false);
		} catch (IOException e) {
			throw new ServiceException ("Cannot insert order history for the order id : " + orderProcessingCreationEntity.getOrderId () ,e);
		}
		try {
			orderDao.deleteBy (orderProcessingCreationEntity.getOrderId());
		} catch (IOException e) {
			throw new ServiceException ("Cannot delete order for : " + orderProcessingCreationEntity.getOrderId () + " from the table PRODUCT\n",e);
		}
	}
	
	@Override
	@Transactional
	public void modifyOrderQuantity (ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) throws ServiceException {
		try {
			recentActivityDao.insert ("Modified", " the order quantity for the Order Id " + modifyOrderQuantityCreationEntity.getOrderId ()  
				+ " from " 
				+ modifyOrderQuantityCreationEntity.getOrderQuantityOld () 
				+ " to " 
				+ modifyOrderQuantityCreationEntity.getOrderQuantityNew ()
				, getInstance ()
			);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
			try {
			productDao.updateProductCountAfterReceivingRequestFromSalesperson (modifyOrderQuantityCreationEntity);
		} catch (IOException e) {
			throw new ServiceException ("Cannot modify product count for productId: " + modifyOrderQuantityCreationEntity.getProductId() + " \n",e);
		}
		try {
			orderDao.updateOrderQuantityAfterReceivingRequestFromSalesperson (modifyOrderQuantityCreationEntity);
		} catch (IOException e) {
			throw new ServiceException ("Cannot update order quantity for orderId: " + modifyOrderQuantityCreationEntity.getOrderId () + " \n",e);
		}
	}
	
	@Override
	@Transactional
	public void reportProductShortageInfo (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws ServiceException {
		try {
			orderDao.reportProductShortageInfo (reportProductShortageInfoEntity);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
}












