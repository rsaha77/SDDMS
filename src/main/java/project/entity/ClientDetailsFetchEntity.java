package project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ClientDetailsFetchEntity {
	private int id;
	private int draw;
	private int start;
	private int length;
}
