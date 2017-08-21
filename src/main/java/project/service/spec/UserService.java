package project.service.spec;

import project.entity.UserAccountCreationEntity;
import project.entity.UserAccountEntity;
import project.entity.UserAccountFetchEntity;
import project.entity.UserAccountListEntity;
import project.exception.ServiceException;

public interface UserService {
	UserAccountEntity getBy (String id) throws ServiceException;

	UserAccountListEntity getBy (UserAccountFetchEntity entity)
			throws ServiceException;

	void insert (UserAccountCreationEntity userAccountCreationEntity)
			throws ServiceException;

	void update (UserAccountCreationEntity userAccountCreationEntity)
			throws ServiceException;

	void deleteBy (String id) throws ServiceException;

	String getLanguage (String id);

	void updateLanguage (String id, String language);
}
