package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailsCreationEntity {
	private String productId;
	private String name;
	private String price;
	private String available;
	private String type;
}
