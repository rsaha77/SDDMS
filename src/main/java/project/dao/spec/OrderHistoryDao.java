package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.dto.OrderHistoryDto;
import project.entity.OrderProcessingCreationEntity;

public interface OrderHistoryDao {
	void insert (OrderProcessingCreationEntity orderProcessingCreationEntity, String status, boolean flag) throws IOException;
	
	List <OrderHistoryDto> getBy (int id, int start, int size) throws IOException;
	
	int getTotalCount (int id) throws IOException;
	
	void deleteBy (String orderId) throws IOException;
}
