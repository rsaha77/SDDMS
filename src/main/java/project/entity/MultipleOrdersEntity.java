package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleOrdersEntity {
	private String clientId;
	private String clientName;
	private String orderDate;
	private OrderItemEntity items[];
}
