package project.service.spec;

import java.util.List;

import project.exception.ServiceException;

public interface ClientRoleService {
	
	List<String> getBy(String userId) throws ServiceException;
	
	void insert(String userId, List<String> roleList) throws ServiceException;
	
	void deleteBy(String userId) throws ServiceException;
}
