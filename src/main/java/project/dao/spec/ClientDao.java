package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.dto.ClientDetailsDto;
import project.entity.ClientDetails;
import project.entity.ClientDetailsEntity;
import project.entity.ClientImportantNoteCreationEntity;
import project.entity.ClientLeadDetailsEntity;
import project.entity.ClientModifyCreationEntity;
import project.entity.DisqualifiedLeadDetailsListEntity;
import project.entity.DisqualifiedLeadEntity;
import project.entity.LeadDetailsEntity;
import project.entity.LeadDetailsListEntity;

public interface ClientDao {
	List <ClientDetailsDto> getBy (int regionId, int start, int size) throws IOException;

	void insertNewClient (ClientDetailsDto clientDetails) throws IOException;

	int getTotalCount (int regionId) throws IOException;
	
	void update (ClientModifyCreationEntity clientModifyCreationEntity) throws IOException;
	
	void deleteBy (String id) throws IOException;
	
	void updateImportantNote (ClientImportantNoteCreationEntity clientImportantNoteCreationEntity) throws IOException;
	
	List <ClientDetails> getClientDetailsList () throws IOException;
	
	ClientDetailsEntity getClientDetailsForOrderId (int orderId);
	
	ClientDetailsEntity getClientDetailsForClientId (String clientId);
	
	DisqualifiedLeadEntity getDisqForClientId (String clientId);
	
	LeadDetailsListEntity getLeadDetailsList ();
	
	LeadDetailsListEntity getLeadDetailsListAll ();
	
	DisqualifiedLeadDetailsListEntity getDisqualifiedLeadDetailsListAll ();
	
	LeadDetailsEntity getLeadDetailsForLeadId (String clientId);
	
	int getLeadCountFromLeadTable ();
	
	List <LeadDetailsEntity> getLeadDetailsList2 ();
	
	List <ClientLeadDetailsEntity> getClientLeadDetailsList ();
	
	boolean isLead (String id);
	
	void disqualifyLead (String clientId);
	
	void setReminderTrueForLeadId (String clientId);
	
	void insertNewLead (LeadDetailsEntity leadDetailsEntity);
	
	void deleteLead (String clientId);
	
	void deleteDisq (String clientId);
	
	void modifyLead (LeadDetailsEntity leadDetailsEntity);
}












