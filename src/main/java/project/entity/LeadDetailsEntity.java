package project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadDetailsEntity {
	private String clientId;
	private String leadName;
	private String leadContact;
	private String leadEmail;
	private String leadAddress;
	private String leadType;
	private String leadTransport;
	private String leadRegion;
	private String leadReminderStatus;
}
