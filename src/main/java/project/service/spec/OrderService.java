package project.service.spec;

import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.OrderDetailsCreationEntity;
import project.entity.OrderDetailsFetchEntity;
import project.entity.OrderDetailsListEntity;
import project.entity.OrderHistoryFetchEntity;
import project.entity.OrderHistoryListEntity;
import project.entity.OrderProcessingCreationEntity;
import project.entity.ReportProductShortageInfoEntity;
import project.exception.ServiceException;

public interface OrderService {
	
	void placeOrder (OrderDetailsCreationEntity ob) throws ServiceException;
	
	OrderDetailsListEntity getOrderBy (OrderDetailsFetchEntity orderDetailsFetchEntity) throws ServiceException;
	
	void processOrder (OrderProcessingCreationEntity orderProcessingCreationEntity) throws ServiceException;
	
	OrderHistoryListEntity getOrderHistoryBy (OrderHistoryFetchEntity orderHistoryFetchEntity) throws ServiceException;
	
	void deleteBy (int orderId) throws ServiceException;
	
	void deleteBy (OrderProcessingCreationEntity orderProcessingCreationEntity) throws ServiceException;
	
	void modifyOrderQuantity (ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) throws ServiceException;
	
	void reportProductShortageInfo (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws ServiceException;
}
