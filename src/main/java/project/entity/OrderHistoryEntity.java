package project.entity;

import project.dto.OrderHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderHistoryEntity {
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
	
	public OrderHistoryEntity (OrderHistoryDto obj) {
		this.status = obj.getStatus ();
		this.orderId = obj.getOrderId ();
		this.clientId = obj.getClientId ();
		this.clientName = obj.getClientName ();
		this.clientContact = obj.getClientContact ();
		this.clientAddress = obj.getClientAddress ();
		this.productId = obj.getProductId ();
		this.productName = obj.getProductName ();
		this.orderQuantity = obj.getOrderQuantity ();
		this.deliveryDate = obj.getDeliveryDate ();
		this.deliveryDate2 = obj.getDeliveryDate2 ();
		this.delayed = obj.getDelayed ();
		this.orderCancellationReason = obj.getOrderCancellationReason ();
	}
}
