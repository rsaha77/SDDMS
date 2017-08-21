package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportProductShortageInfoEntity {
	private String productId;
	private String productName;
	private String dateOfDelivery;
	private String clientAffected;
	private String noteToTheManager;
}
