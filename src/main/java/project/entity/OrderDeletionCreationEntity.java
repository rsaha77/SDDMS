package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDeletionCreationEntity {
	private int orderId;
	private String productId;
	private String clientId;
	private String clientName;
	private String productName;
	private String orderQuantity;
	private String reasonManual;
	private String reasonOther;
}
