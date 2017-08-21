package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistoryDto {
	private String status;
	private int orderId;
	private String clientId;
	private String clientName;
	private String clientContact;
	private String clientAddress;
	private String productId;
	private String productName;
	private String orderQuantity;
	private String deliveryDate;
	private String deliveryDate2;
	private String delayed;
	private String orderCancellationReason;
}
