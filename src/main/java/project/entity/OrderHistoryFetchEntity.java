package project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderHistoryFetchEntity {
	private int id;
	private int draw;
	private int start;
	private int length;
}
