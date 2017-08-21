package project.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import project.dao.spec.ClientDao;
import project.dao.spec.ProductDao;
import project.dao.spec.RegionDao;
import project.dao.spec.TypeDao;
import project.dto.ClientDetailsDto;
import project.entity.ClientDetailsCreationEntity;
import project.entity.ClientDetailsEntity;
import project.entity.ClientDetailsFetchEntity;
import project.entity.ClientDetailsListEntity;
import project.entity.ClientImportantNoteCreationEntity;
import project.entity.ClientDetails;
import project.entity.ClientModifyCreationEntity;
import project.exception.ServiceException;
import project.service.spec.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private TypeDao typeDao;
	
	@Autowired
	private RegionDao regionDao;

	@Override
	public ClientDetailsListEntity getBy (ClientDetailsFetchEntity entity) throws ServiceException {
		List <ClientDetailsDto> details = null; // All the rows of client-table will be stored in the variable "details"
		int clientCountByClientId;
		try {
			details = clientDao.getBy (entity.getId(), entity.getStart (), entity.getLength ());
			clientCountByClientId = clientDao.getTotalCount (entity.getId ());
		} catch (IOException e) {
			throw new ServiceException ("Cannot find client for clientId: " + entity.getId(), e);
		}

		List <ClientDetailsEntity> entities = Lists.newArrayList ();
		for (ClientDetailsDto detail : details) {
			ClientDetailsEntity clientDetailsEntity = new ClientDetailsEntity (detail);
			entities.add (clientDetailsEntity);
		}
		return new ClientDetailsListEntity (entity.getDraw (), clientCountByClientId, clientCountByClientId, entities);
	}
	
	@Override
	@Transactional
	public void insertNewClient (ClientDetailsCreationEntity clientDetailsCreationEntity) throws ServiceException {
		
		/* _______________ INSERT NEW TYPE TO TYPE_TABLE__________________ */
		
		if (clientDetailsCreationEntity.getTypeManual ().equals ("others")) {
			try {
				typeDao.insertNewType (clientDetailsCreationEntity.getTypeOther ());
			} catch (IOException e) {
				throw new ServiceException ("Cannot add type : " + clientDetailsCreationEntity.getTypeOther (), e);
			}
		}
		
		/* _______________ INSERT NEW REGION TO REGION_TABLE ________________ */
		
		if (clientDetailsCreationEntity.getRegionManual ().equals ("others")) {
			try {
				regionDao.insertNewRegion (clientDetailsCreationEntity.getRegionOther ());
			} catch (IOException e) {
				throw new ServiceException ("Cannot add type : " + clientDetailsCreationEntity.getTypeOther (), e);
			}
		}
		
		ClientDetailsDto clientDetailsDto = new ClientDetailsDto (clientDetailsCreationEntity);
		
		/* _______________________ ADD THE NEW CLIENT TO CLIENT TABLE _______________________ */
		
		try {
			clientDao.insertNewClient (clientDetailsDto);
		} catch (IOException e) {
			throw new ServiceException("Cannot add client details for clientId: " + clientDetailsCreationEntity.getClientId(), e);
		}
		
		
		/* _______________________ ADD THE NEW CLIENT_ID TO THE POTENTIAL CLIENT OF THE PRODUCTS WHICH IS MATCING IT'S TYPE _______________________ */
		
	}	

	@Override
	public void modify (ClientModifyCreationEntity clientModifyCreationEntity) throws ServiceException {
		try {
			clientDao.update (clientModifyCreationEntity);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
		} catch (IOException e) {
			throw new ServiceException("Cannot edit client detail for clientId: " + clientModifyCreationEntity.getClientId (), e);
		}
	}
	

	@Override
	public void deleteBy (String id) throws ServiceException {
		try {
			clientDao.deleteBy (id);
		} catch (IOException e) {
			throw new ServiceException("Cannot delete client detail for clientId: " + id, e);																																																																																													
		}
	}
	
	@Override
	public void updateImportantNote (ClientImportantNoteCreationEntity clientImportantNoteCreationEntity) throws ServiceException {
		try {
			clientDao.updateImportantNote (clientImportantNoteCreationEntity);
		} catch (IOException e) {
			throw new ServiceException ("Cannot update important note for clientId: " + clientImportantNoteCreationEntity.getClid (), e);
		}
	}
	
	@Override
	public List <ClientDetails> getClientDetailsList () {
		List <ClientDetails> clientList = null;
		try {
			clientList = clientDao.getClientDetailsList ();
		} catch (IOException e) {
			throw new ServiceException (e);
		}
		return clientList;
	}
}











