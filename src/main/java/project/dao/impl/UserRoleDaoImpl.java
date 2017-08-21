package project.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.UserRoleDao;
import project.dto.RoleDto;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

	@Autowired
	private JdbcTemplate template;

	private static final String FETCH_BY_ID = "SELECT * FROM USER_ROLE WHERE USER_ACCOUNT_ID = ?";

	private static final String INSERT = "INSERT INTO USER_ROLE (user_account_id, role) "
			+ " VALUES (? , ?)";

	private static final String DELETE = "DELETE FROM USER_ROLE WHERE user_account_id = ?";

	@Override
	public List<RoleDto> getBy(String userId) throws IOException {
		try {
			return template.query(FETCH_BY_ID, ps -> ps.setString(1, userId), (
					rs, rownum) -> {
				return new RoleDto(rs.getString("role"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void insert(String userId, List<RoleDto> roleList)
			throws IOException {
		try {
			template.batchUpdate(INSERT, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					ps.setString(1, userId);
					ps.setString(2, roleList.get(i).getName());
				}

				@Override
				public int getBatchSize() {
					return roleList.size();
				}
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deleteBy(String userId) throws IOException {
		try {
			template.update(DELETE, ps -> ps.setString(1, userId));
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
}
