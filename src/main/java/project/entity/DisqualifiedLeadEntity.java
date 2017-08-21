package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisqualifiedLeadEntity {
	private String clientId;
	private String disLeadName;
	private String disLeadContact;
	private String disLeadEmail;
	private String disLeadAddress;
	private String disLeadType;
	private String disLeadTransport;
	private String disLeadRegion;
}
