package project.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import project.dto.UserAccountDto;

@NoArgsConstructor
@Data
public class UserAccountCreationEntity {
	private String id;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roles;
	private int regionId;
	
	public UserAccountCreationEntity(UserAccountDto userAccount, List<String> roles, int officeId) {
		this.id = userAccount.getId();
		this.password = userAccount.getPassword();
		this.firstName = userAccount.getFirstName();
		this.lastName = userAccount.getLastName();
		this.email = userAccount.getEmail();
		this.roles = roles;
	}
}
