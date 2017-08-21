package project.dto;

import project.entity.ClientDetailsCreationEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

// This layer is used for "transferring data within the system". Not related to the view.
// Here, the object of this class is used for talking to the database.

public class ClientDetailsDto {
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
	
	public ClientDetailsDto (ClientDetailsCreationEntity entity) {
		this.clientId = entity.getClientId ();
		this.name = entity.getName ();
		this.email = entity.getEmail ();
		String tm = entity.getTypeManual ();
		String to = entity.getTypeOther ();
		if (tm.equals ("others")) {
			this.type = to;
		} else {
			this.type = tm;
		}
		this.address = entity.getAddress ();
		this.contact = entity.getContact ();
		this.transport = entity.getTransport ();
		this.nextOrderDate = entity.getNextOrderDate ();
		this.importantNote = entity.getImportantNote ();
		String rm = entity.getRegionManual ();
		String ro = entity.getRegionOther ();
		if (rm.equals ("others")) {
			this.region = ro;
		} else {
			this.region = rm;
		}
	}
}








