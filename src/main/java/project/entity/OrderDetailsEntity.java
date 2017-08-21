package project.entity;

import project.dto.OrderDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDetailsEntity {
	private int orderId;
	private String clientId;
	private String clientName;
	private String proId;
	private String productName;
	private String orderQuantity;
	private String orderDate;
	private String transport;
	private String status;
	
	public OrderDetailsEntity (OrderDetailsDto ob) {
		this.orderId = ob.getOrderId ();
		this.clientId = ob.getClientId ();
		this.clientName = ob.getClientName ();
		this.proId = ob.getProId ();
		this.productName = ob.getProductName ();
		this.orderQuantity = ob.getOrderQuantity ();
		this.orderDate = ob.getOrderDate ();
		this.transport = ob.getTransport ();
		this.status = ob.getStatus ();
	}
}
