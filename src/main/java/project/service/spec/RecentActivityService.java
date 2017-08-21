package project.service.spec;

import java.util.List;

import project.entity.RecentActivityEntity;
import project.exception.ServiceException;

public interface RecentActivityService {
	List <RecentActivityEntity> getRecentActivityList () throws ServiceException;
}
