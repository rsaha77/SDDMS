package project.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.spec.OrderHistoryDao;
import project.dao.spec.RecentActivityDao;
import project.exception.ServiceException;
import project.service.spec.OrderHistoryService;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
	
	@Autowired
	private OrderHistoryDao orderHistoryDao;
	
	@Autowired
	private RecentActivityDao recentActivityDao;
	
	public String getInstance () {
		DateFormat df = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();   
		return df.format(today);
	}
	
	@Override
	public void deleteBy (String orderId) throws ServiceException {
		try {
			recentActivityDao.insert ("Deleted", " the order history with order Id (" + orderId + ")", getInstance ());
		} catch (IOException e) {
			throw new ServiceException (e);
		}
		try {
			orderHistoryDao.deleteBy (orderId);
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
}
