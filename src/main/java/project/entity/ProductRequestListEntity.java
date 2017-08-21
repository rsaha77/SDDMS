package project.entity;

import java.util.List;

import project.entity.ProductRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestListEntity {
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List <ProductRequestEntity> productRequestEntity;
}

