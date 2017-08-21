package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductShortageEntityForManager {
	private String productId;
	private String productName;
	private int shortageQuantity;
	private String status;
	private String comments;
	private String clientAffected;
	private String clientAffectedId;
	private String nearestDateOfDelivery;
	private String noteToTheManager;
}
