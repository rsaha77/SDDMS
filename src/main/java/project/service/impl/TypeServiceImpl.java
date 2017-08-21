package project.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.spec.TypeDao;
import project.dto.TypeDto;
import project.entity.TypeEntity;
import project.entity.TypeFetchEntity;
import project.exception.ServiceException;
import project.service.spec.TypeService;

@Service
public class TypeServiceImpl implements TypeService {
	@Autowired
	private TypeDao typeDao;
	
	@Override
	public TypeEntity getBy (TypeFetchEntity entity) throws ServiceException {
		try {
			int typeSize = typeDao.getTotalCount ();
			List <TypeDto> typeData = typeDao.getAll (entity.getStart(), entity.getLength());
			return new TypeEntity (entity.getDraw (), typeSize, typeSize, typeData);
			
		} catch (IOException e) {
			throw new ServiceException ("Cannot get new types", e);
		}
	}
}
