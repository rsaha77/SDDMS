package project.service.spec;

import project.entity.TypeEntity;
import project.entity.TypeFetchEntity;
import project.exception.ServiceException;

public interface TypeService {
	TypeEntity getBy (TypeFetchEntity entity) throws ServiceException;
}
