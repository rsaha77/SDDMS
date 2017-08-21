package project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductDetailsFetchEntity {
	private String clientIdForRecommendation;
	private int draw;
	private int start;
	private int length;
}
