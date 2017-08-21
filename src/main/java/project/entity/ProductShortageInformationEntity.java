package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductShortageInformationEntity {
	private String productId;
	private String productName;
	private String clientAffected;
	private String clientAffectedId;
	private int underflowQuantity;
	private String dateOfDelivery;
}
