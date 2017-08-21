package project.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.spec.RecentActivityDao;
import project.entity.RecentActivityEntity;
import project.exception.ServiceException;
import project.service.spec.RecentActivityService;

@Service
public class RecentActivityServiceImpl implements RecentActivityService {
	
	@Autowired
	RecentActivityDao recentActivityDao;
	
	@Override
	public List <RecentActivityEntity> getRecentActivityList () throws ServiceException {
		try {
			return recentActivityDao.getRecentActivityList ();
		} catch (IOException e) {
			throw new ServiceException (e);
		}
	}
}
