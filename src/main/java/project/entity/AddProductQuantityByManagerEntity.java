package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class AddProductQuantityByManagerEntity {
	private String productId;
	private String productName;
	private String quantity;
}
