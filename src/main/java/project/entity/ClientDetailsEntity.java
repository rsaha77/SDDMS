package project.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dto.ClientDetailsDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDetailsEntity implements Serializable {

	private static final long serialVersionUID = -7395917071437157624L;
	
	// Below data members naming should be same as that of client.js
	
	private String clientId;
	private String name;
	private String email;
	private String type;
	private String address;
	private String contact;
	private String transport;
	private String nextOrderDate;
	private String importantNote;
	private String region;
	
	public ClientDetailsEntity (ClientDetailsDto clientDetails) {
		this.clientId = clientDetails.getClientId();
		this.name = clientDetails.getName ();
		this.email = clientDetails.getEmail();
		this.type = clientDetails.getType ();
		this.address = clientDetails.getAddress();
		this.contact = clientDetails.getContact();
		this.transport = clientDetails.getTransport();
		this.nextOrderDate = clientDetails.getNextOrderDate();
		this.importantNote = clientDetails.getImportantNote();
		this.region = clientDetails.getRegion();  //Because of "this.region = region" region had a null value; 
	}
	
	
//	getId(), getPassword(), getRoles()           
	
	
//	public UserAccountEntity (UserAccountDto userAccount, List<String> roles,
//			String officeName) {
//		this.id = userAccount.getId();
//		this.password = userAccount.getPassword();
//		this.firstName = userAccount.getFirstName();
//		this.lastName = userAccount.getLastName();
//		this.email = userAccount.getEmail();
//		this.roles = roles;
//		this.officeName = officeName;
//	}
}
