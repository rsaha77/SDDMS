package project.service.spec;

import project.entity.RegionEntity;
import project.entity.RegionFetchEntity;
import project.exception.ServiceException;


public interface RegionService {
	RegionEntity getBy (RegionFetchEntity entity) throws ServiceException;
}
