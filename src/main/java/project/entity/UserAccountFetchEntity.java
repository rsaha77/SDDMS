package project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserAccountFetchEntity {

	private int officeId;
	private int draw;
	private int start;
	private int length;
}
