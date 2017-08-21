package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmergencyReplacementFetchEntity {
	private String productId;
	private String clientId;
	private String productType;
}
