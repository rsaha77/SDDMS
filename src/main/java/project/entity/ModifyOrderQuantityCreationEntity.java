package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifyOrderQuantityCreationEntity {
	private int orderId;
	private String productId;
	private String orderQuantityOld;
	private String orderQuantityNew;
}
