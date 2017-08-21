package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ClientModifyCreationEntity {
	private String clientId;
	private String name;
	private String email;
	private String address;
	private String contact;
	private String nextOrderDate;
}
