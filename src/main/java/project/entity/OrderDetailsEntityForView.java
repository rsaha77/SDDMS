package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDetailsEntityForView {
	private int orderId;
	private String dateOfDelivery;
	private String clientName;
	private String clientType;
	private String clientTransport;
	private String clientPhone;
	private String clientEmail;
	private String clientAddress;
	private OrderItemEntity items[];
}
