package project.dto;

import project.entity.ProductDetailsCreationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailsDto {
	private String productId;
	private String name;
	private String price;
	private String type;
	private String season;
	private String isNew;
	private String available;
	private String ordered;
	private String remaining;
	
	public ProductDetailsDto (ProductDetailsCreationEntity entity) {
		this.productId = entity.getProductId ();
		this.name = entity.getName ();
		this.price = entity.getPrice ();
		this.available = entity.getAvailable ();
		this.type = entity.getType ();
	}
}
