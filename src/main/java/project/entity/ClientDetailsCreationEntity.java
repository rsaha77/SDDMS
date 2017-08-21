package project.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dto.ClientDetailsDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDetailsCreationEntity {
	private String clientId;
	private String name;
	private String email;
	private String typeManual;
	private String typeOther;
	private String address;
	private String contact;
	private String transport;
	private String nextOrderDate;
	private String importantNote;
	private String regionManual;
	private String regionOther;
	
	public ClientDetailsCreationEntity (ClientDetailsDto clientDetails, int clientId) {
		this.clientId = clientDetails.getClientId ();
		this.name = clientDetails.getName ();
		this.email = clientDetails.getEmail ();
		this.typeManual = clientDetails.getType ();
		this.address = clientDetails.getAddress ();
		this.contact = clientDetails.getContact ();
		this.transport = clientDetails.getTransport ();
		this.nextOrderDate = clientDetails.getNextOrderDate ();
		this.importantNote = clientDetails.getImportantNote ();
		this.regionManual = clientDetails.getRegion ();
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}
}
