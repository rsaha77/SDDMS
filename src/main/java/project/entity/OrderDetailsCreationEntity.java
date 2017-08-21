package project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailsCreationEntity {
	private int orderId;
	private String clientId;
	private String clientName;
	private String proId;
	private String productName;
	private String orderQuantity;
	private String orderDate;
}
