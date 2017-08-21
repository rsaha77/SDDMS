package project.entity;

import project.dto.ProductDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductDetailsEntity {
	private String productId;
	private String name;
	private String price;
	private String type;
	private String season;
	private String isNew;
	private String available;
	private String ordered;
	private String remaining;
	
	public ProductDetailsEntity (ProductDetailsDto ob) {
		this.productId = ob.getProductId ();
		this.name = ob.getName ();
		this.price = ob.getPrice ();
		this.type = ob.getType ();
		this.season = ob.getSeason ();
		this.isNew = ob.getIsNew ();
		this.available = ob.getAvailable ();
		this.ordered = ob.getOrdered ();
		this.remaining = ob.getRemaining ();
	}
}
