package project.service.spec;

import project.exception.ServiceException;

public interface OrderHistoryService {
	void deleteBy (String orderId) throws ServiceException;
}
