package project.service.spec;


import java.util.List;

import project.entity.ClientDetailsCreationEntity;
import project.entity.ClientDetailsFetchEntity;
import project.entity.ClientDetailsListEntity;
import project.entity.ClientImportantNoteCreationEntity;
import project.entity.ClientDetails;
import project.entity.ClientModifyCreationEntity;
import project.entity.LeadDetailsEntity;
import project.exception.ServiceException;

public interface ClientService {

	ClientDetailsListEntity getBy (ClientDetailsFetchEntity clientDetailsFetchEntity) throws ServiceException;
	
	void insertNewClient (ClientDetailsCreationEntity clientDetailsCreationEntity) throws ServiceException;
	
	void modify (ClientModifyCreationEntity clientModifyCreationEntity) throws ServiceException;
	
	void deleteBy (String id) throws ServiceException;
	
	void updateImportantNote (ClientImportantNoteCreationEntity clientImportantNoteCreationEntity) throws ServiceException;
	
	List <ClientDetails> getClientDetailsList () throws ServiceException;
}