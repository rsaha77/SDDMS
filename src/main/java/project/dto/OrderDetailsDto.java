package project.dto;

import project.entity.OrderDetailsCreationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailsDto {
	private int orderId;
	private String clientId;
	private String clientName;
	private String proId;
	private String productName;
	private String orderQuantity;
	private String orderDate;
	private String transport;
	private String status;
	
	public OrderDetailsDto (OrderDetailsCreationEntity ob) {
		this.orderId = ob.getOrderId ();
		this.clientId = ob.getClientId ();
		this.clientName = ob.getClientName ();
		this.proId = ob.getProId ();
		this.productName = ob.getProductName ();
		this.orderQuantity = ob.getOrderQuantity ();
		this.orderDate = ob.getOrderDate();
	}
}
