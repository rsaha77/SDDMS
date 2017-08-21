package project.dao.spec;

import java.util.List;
import java.io.IOException;

import project.dto.OrderDetailsDto;
import project.entity.ModifyOrderQuantityCreationEntity;
import project.entity.MultipleOrdersEntity;
import project.entity.OrderDetailsEntity;
import project.entity.OrderDetailsEntityForView;
import project.entity.ReportProductShortageInfoEntity;

public interface OrderDao {
	
	void placeOrder (OrderDetailsDto orderDetails) throws IOException;
	
	List <OrderDetailsDto> getBy (int orderId, int start, int size) throws IOException;
	
	int getTotalCount (int orderId) throws IOException;
	
	void deleteBy (int orderId) throws IOException;
	
	void updateOrderQuantityAfterReceivingRequestFromSalesperson (ModifyOrderQuantityCreationEntity modifyOrderQuantityCreationEntity) throws IOException;
	
	void reportProductShortageInfo (ReportProductShortageInfoEntity reportProductShortageInfoEntity) throws IOException;
	
	public  OrderDetailsEntityForView getOrderDetailsForOrderId (int orderId);
	
	void placeOrderMultiple (MultipleOrdersEntity multipleOrdersQuantityEntity);
}
