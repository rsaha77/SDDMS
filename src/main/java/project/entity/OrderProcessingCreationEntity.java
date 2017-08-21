
package project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderProcessingCreationEntity {
	private int orderId;
	private String clientId;
	private String clientName;
	private String productId;
	private String productName;
	private String orderQuantity;
	private String transport;
	private String orderDate;
	private String orderDate2;
	private String reasonManual;
	private String reasonOther;
}
