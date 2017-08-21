package project.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import project.dao.spec.UserDao;
import project.dto.UserAccountDto;
import project.entity.UserAccountCreationEntity;
import project.entity.UserAccountEntity;
import project.entity.UserAccountFetchEntity;
import project.entity.UserAccountListEntity;
import project.exception.ServiceException;
import project.service.spec.UserRoleService;
import project.service.spec.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleService userRoleService;

	public UserAccountEntity getBy(String id) throws ServiceException {
		UserAccountDto account = null;
		try {
			account = userDao.getBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot find user for id: " + id, e);
		}

		List<String> roles = null;
//		String regionName = null;
		UserAccountEntity userAccountEntity = null;
		if (account != null) {
			roles = userRoleService.getBy(id);
//			regionName = RegionService.getBy(account.getRegionId());
			userAccountEntity = new UserAccountEntity(account, roles);
		}
		return userAccountEntity;
	}

	@Override
	public UserAccountListEntity getBy(
			UserAccountFetchEntity entity) throws ServiceException {
		List<UserAccountDto> accounts = null;
		int userCountByOfficeId;
		try {
			accounts = userDao.getBy(entity.getOfficeId(), entity.getStart(), entity.getLength());
			userCountByOfficeId = userDao.getTotalCount(entity.getOfficeId());
		} catch (IOException e) {
			throw new ServiceException("Cannot find user for officeId: "
					+ entity.getOfficeId(), e);
		}

		List<UserAccountEntity> entities = Lists.newArrayList();
		for (UserAccountDto account : accounts) {
			List<String> roles = userRoleService.getBy (account.getId ());
//			String regionName = RegionService.getBy (account .getRegionId());
			UserAccountEntity userAccountEntity = new UserAccountEntity (account, roles);
			entities.add(userAccountEntity);
		}

		return new UserAccountListEntity(entity.getDraw(), userCountByOfficeId,
				userCountByOfficeId, entities);
	}

	@Override
	@Transactional
	public void insert(
			UserAccountCreationEntity userAccountCreationEntity)
			throws ServiceException {
		String userId = userAccountCreationEntity.getId();

		// Insert user account
		UserAccountDto userAccount = new UserAccountDto(userAccountCreationEntity);
		
		try {
			userDao.insert(userAccount);
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for userId: "
					+ userAccountCreationEntity.getId(), e);
		}

		// Delete & Insert roles
		userRoleService.deleteBy(userId);
		userRoleService.insert(userId,
				userAccountCreationEntity.getRoles());

	}

	@Override
	public void update(UserAccountCreationEntity userAccountCreationEntity) throws ServiceException {
		String userId = userAccountCreationEntity.getId();
		
		if (StringUtils.isEmpty(userAccountCreationEntity.getPassword())) {
			userAccountCreationEntity.setPassword(null);
		}
		
		// Insert user account
		UserAccountDto userAccount = new UserAccountDto(userAccountCreationEntity);
		
		try {
			userDao.update(userAccount);
		} catch (IOException e) {
			throw new ServiceException("Cannot edit user account for userId: " + userAccountCreationEntity.getId(), e);
		}
		
		// Delete & Insert roles
		userRoleService.deleteBy(userId);
		userRoleService.insert(userId, userAccountCreationEntity.getRoles());
	}

	@Override
	public void deleteBy(String id) throws ServiceException {
		try {
			userDao.deleteBy(id);
			userRoleService.deleteBy(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot delete user account for userId: " + id, e);
		}
	}

	@Override
	public String getLanguage(String id) {
		try {
			return userDao.getLanguage(id);
		} catch (IOException e) {
			throw new ServiceException(
					"Cannot get language for user with id : " + id, e);
		}
	}

	@Override
	public void updateLanguage(String id, String language) {
		try {
			userDao.updateLanguage(id, language);
		} catch (IOException e) {
			throw new ServiceException(
					"Cannot update language for user with id : " + id, e);
		}
	}
}
