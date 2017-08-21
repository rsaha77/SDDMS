package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.dto.RoleDto;

public interface ClientRoleDao {
	List<RoleDto> getBy(String userId) throws IOException;
	void insert(String userId, List<RoleDto> roleList) throws IOException;
	void deleteBy(String userId) throws IOException; 
}
