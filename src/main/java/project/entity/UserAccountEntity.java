package project.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import project.dto.UserAccountDto;

@NoArgsConstructor
@Data
public class UserAccountEntity implements Serializable {

	private static final long serialVersionUID = -7395917071437157624L;

	private String id;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roles;

	public UserAccountEntity(UserAccountDto userAccount, List<String> roles) {
		this.id = userAccount.getId();
		this.password = userAccount.getPassword();
		this.firstName = userAccount.getFirstName();
		this.lastName = userAccount.getLastName();
		this.email = userAccount.getEmail();
		this.roles = roles;
	}
}
