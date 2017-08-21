package project.controller;
//import static java.lang.System.out;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.dao.spec.ClientDao;
import project.entity.ClientDetails;
import project.entity.ClientDetailsCreationEntity;
import project.entity.ClientDetailsEntity;
import project.entity.ClientDetailsFetchEntity;
import project.entity.ClientDetailsListEntity;
import project.entity.ClientImportantNoteCreationEntity;
import project.entity.ClientLeadDetailsEntity;
import project.entity.ClientModifyCreationEntity;
import project.entity.DisqualifiedLeadDetailsListEntity;
import project.entity.DisqualifiedLeadEntity;
import project.entity.LeadDetailsEntity;
import project.entity.LeadDetailsListEntity;
import project.service.spec.ClientService;

@Controller
public class ClientController {
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ClientDao clientDao;
	
	@RequestMapping (value = "/clientmanagement")
	public String clientmanagement () {
		return "clientmanagement";
	}
	
	@RequestMapping (value = "/userProfile")
	public String temp () {
		return "userProfile";
	}
	
	
	@RequestMapping (value = "/clientmanagement/findClientByClientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ClientDetailsListEntity findClientByClientId (@RequestBody ClientDetailsFetchEntity entity) {
		ClientDetailsListEntity ob =  clientService.getBy (entity);
		return ob;
	}

	
	@RequestMapping (value = "/clientmanagement/addClientDetails", method = RequestMethod.POST)
	@ResponseBody
	public void addClientDetails (@RequestBody ClientDetailsCreationEntity clientDetailsCreationEntity) {
		clientService.insertNewClient (clientDetailsCreationEntity);
	}
	
	@RequestMapping (value = "/clientmanagement/modifyClientDetails", method = RequestMethod.POST)
	@ResponseBody
	public void modifyClientDetails (@RequestBody ClientModifyCreationEntity clientModifyCreationEntity) {
//		out.println ("UPDATE");
		clientService.modify (clientModifyCreationEntity);
	}
	
	
	@RequestMapping (value = "/clientmanagement/modifyLead", method = RequestMethod.POST)
	@ResponseBody
	public void modifyLead (@RequestBody LeadDetailsEntity leadDetailsEntity) {
		clientDao.modifyLead (leadDetailsEntity);
	}
	
	
	@RequestMapping(value = "/clientmanagement/deleteClientDetails", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserAccount (@RequestParam String id) {
		clientService.deleteBy(id);
	}
	
	
	@RequestMapping(value = "/clientmanagement/deleteDisq", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteDisq (@RequestParam String clientId) {
		clientDao.deleteDisq (clientId);
	}
	
	@RequestMapping(value = "/clientmanagement/deleteLead", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteLead (@RequestParam String clientId) {
		clientDao.deleteLead (clientId);
	}
	
	@RequestMapping(value = "/clientmanagement/updateImportantNote", method = RequestMethod.POST)
	@ResponseBody
	public void updateImportantNote (@RequestBody ClientImportantNoteCreationEntity clientImportantNoteCreationEntity) {
//		out.println ("NOTE");
//		out.println (clientImportantNoteCreationEntity);
		clientService.updateImportantNote (clientImportantNoteCreationEntity);
	}
	
	
	@RequestMapping (value = "/clientmanagement/getClientForOrderId", method = RequestMethod.POST)
	@ResponseBody
	public ClientDetailsEntity getClientDetailsForOrderId (@RequestParam int orderId) {
		return clientDao.getClientDetailsForOrderId (orderId);
	}
	
	@RequestMapping (value = "/clientmanagement/getClientForClientId", method = RequestMethod.POST)
	@ResponseBody
	public ClientDetailsEntity getClientDetailsForClientId (@RequestParam String clientId) {
		return clientDao.getClientDetailsForClientId (clientId);
	}
	
	
	@RequestMapping (value = "/clientmanagement/getDisqForClientId", method = RequestMethod.POST)
	@ResponseBody
	public DisqualifiedLeadEntity getDisqForClientId (@RequestParam String clientId) {
		return clientDao.getDisqForClientId (clientId);
	}
	
	
	@RequestMapping (value = "/clientmanagement/fetchLeadClients", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public LeadDetailsListEntity getLeadDetailsList (@RequestBody ClientDetailsFetchEntity entity) {
		LeadDetailsListEntity ob =  clientDao.getLeadDetailsList ();
//		out.println (ob);
		return ob;
	}
	
	@RequestMapping (value = "/clientmanagement/fetchLeadClientsAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public LeadDetailsListEntity getLeadDetailsListAll (@RequestBody ClientDetailsFetchEntity entity) {
		LeadDetailsListEntity ob =  clientDao.getLeadDetailsListAll ();
//		out.println (ob);
		return ob;
	}
	
	
	
	@RequestMapping (value = "/clientmanagement/fetchDisqualifiedLeadClientsAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DisqualifiedLeadDetailsListEntity getDisqualifiedLeadDetailsListAll (@RequestBody ClientDetailsFetchEntity entity) {
		DisqualifiedLeadDetailsListEntity ob =  clientDao.getDisqualifiedLeadDetailsListAll ();
//		out.println (ob);
		return ob;
	}
	
	
	@RequestMapping (value = "/getLeadForLeadId", method = RequestMethod.POST)
	@ResponseBody
	public LeadDetailsEntity getLeadForLeadId (@RequestParam String clientId) {
		return clientDao.getLeadDetailsForLeadId (clientId);
	}
	
	@RequestMapping (value = "/clientmanagement/getLeadCountFromLeadTable", method = RequestMethod.POST)
	@ResponseBody
	public int getLeadCountFromLeadTable () {
		return clientDao.getLeadCountFromLeadTable ();
	}
	
	
	@RequestMapping (value = "/clientmanagement/getClientDetailsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <ClientDetails> getClientDetailsList () {
		List <ClientDetails> clientDetailsList = clientService.getClientDetailsList ();
		return clientDetailsList;
	}
	
	
	@RequestMapping (value = "/clientmanagement/getLeadDetailsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <LeadDetailsEntity> getLeadDetailsList () {
		List <LeadDetailsEntity> leadDetailsList = clientDao.getLeadDetailsList2 ();
//		out.println (leadDetailsList);
		return leadDetailsList;
	}
	
	@RequestMapping (value = "/clientmanagement/getClientAndLeadDetailsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List <ClientLeadDetailsEntity> getClientAndLeadDetailsList () {
		List <ClientLeadDetailsEntity> clientLeadDetailsList = clientDao.getClientLeadDetailsList ();
//		out.println (clientLeadDetailsList);
		return clientLeadDetailsList;
	}
	
	@RequestMapping (value = "/isLead", method = RequestMethod.POST)
	@ResponseBody
	public boolean isLead (@RequestParam String id) {
		return clientDao.isLead (id);
	}
	
	@RequestMapping (value = "/disqualifyLeadAndDeleteFromCal", method = RequestMethod.POST)
	@ResponseBody
	public void disqualifyLeadAndDeleteFromCal (@RequestParam String clientId) {
		// Insert into disqualified leads (For Special Attention).. Delete from lead .. And delete the lead from lead table.
		clientDao.disqualifyLead (clientId);
	}

	
	@RequestMapping (value = "/clientmanagement/addLeadDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addLeadDetails (@RequestBody LeadDetailsEntity leadDetailsEntity) {
		clientDao.insertNewLead (leadDetailsEntity);
	}
}














