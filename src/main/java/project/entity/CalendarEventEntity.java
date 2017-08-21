package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalendarEventEntity {
	private String title;
	private String start;
	private String clientId;
	private String productId;
	private int orderId;
}
