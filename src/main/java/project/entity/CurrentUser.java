package project.entity;

import java.util.List;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {

	private static final long serialVersionUID = -4568931000872570880L;

	private UserAccountEntity user;

	public CurrentUser (UserAccountEntity user) {
		super (user.getId(), user.getPassword(), AuthorityUtils
				.createAuthorityList(user.getRoles().toArray(
						new String[user.getRoles().size()])));
		this.user = user;
	}

	public UserAccountEntity getUser() {
		return user;
	}

	public String getId() {
		return user.getId();
	}

	public List<String> getRole() {
		return user.getRoles();
	}
}
