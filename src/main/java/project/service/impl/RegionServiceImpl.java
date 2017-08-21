package project.service.impl;

import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.spec.RegionDao;
import project.dto.RegionDto;
import project.entity.RegionEntity;
import project.entity.RegionFetchEntity;
import project.exception.ServiceException;
import project.service.spec.RegionService;

@Service
public class RegionServiceImpl implements RegionService {
	
	@Autowired
	private RegionDao regionDao;
	
	@Override
	public RegionEntity getBy (RegionFetchEntity entity) throws ServiceException {
		try {
			int regionSize = regionDao.getTotalCount ();
			List <RegionDto> regionData = regionDao.getAll (entity.getStart(), entity.getLength());
			return new RegionEntity (entity.getDraw (), regionSize, regionSize, regionData);
			
		} catch (IOException e) {
			throw new ServiceException ("Cannot get new regions", e);
		}
	}
}
