package project.service.spec;

import org.springframework.security.core.userdetails.UserDetailsService;

import project.entity.CurrentUser;
import project.exception.ServiceException;


public interface CurrentUserDetailsService extends UserDetailsService {
	
	CurrentUser loadUserByUsername (String id) throws ServiceException;
}
