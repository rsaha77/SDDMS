package project.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.UserDao;
import project.dto.UserAccountDto;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate template;

	private static final String FETCH_BY_ID = "SELECT * FROM USER_ACCOUNT WHERE id = ?";

	private static final String FETCH = "SELECT * FROM USER_ACCOUNT LIMIT ? OFFSET ?";
	
	private static final String FETCH_BY_OFFICE = "SELECT * FROM USER_ACCOUNT WHERE office_id = ? LIMIT ? OFFSET ?";

	private static final String USER_COUNT_BY_OFFICE = "SELECT COUNT(*) FROM USER_ACCOUNT WHERE office_id = ?";
	
	private static final String USER_COUNT = "SELECT COUNT(*) FROM USER_ACCOUNT";

	private static final String INSERT_USER = "INSERT INTO USER_ACCOUNT "
			+ " (id, password, firstname, lastname, email, office_id)"
			+ " VALUES (?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_USER_AND_PASSWORD = "UPDATE USER_ACCOUNT SET password = ?, "
			+ " firstname = ?, lastname = ?, email = ?, office_id = ? WHERE id = ?";

	private static final String UPDATE_USER = "UPDATE USER_ACCOUNT SET  "
			+ " firstname = ?, lastname = ?, email = ?, office_id = ? WHERE id = ?";

	private static final String DELETE_USER = "DELETE FROM USER_ACCOUNT WHERE id = ?";

	private static final String GET_USER_LANGUAGE = "SELECT LANGUAGE FROM USER_ACCOUNT where id = ? ";

	private static final String UPDATE_USER_LANGUAGE = "UPDATE USER_ACCOUNT SET language = ? WHERE id = ?";

	@Override
	public UserAccountDto getBy(String id) throws IOException {
		try {
			return template.queryForObject(
					FETCH_BY_ID,
					(rs, rownum) -> {
						return new UserAccountDto(rs.getString("id"), rs
								.getString("password"), rs
								.getString("firstname"), rs
								.getString("lastname"), rs.getString("email"),
								rs.getInt("office_id"));
					}, id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<UserAccountDto> getBy(int officeId, int start, int size)
			throws IOException {
		try {
			if (officeId <= 0) {
				return template.query(
						FETCH,
						ps -> {
							ps.setInt(1, size);
							ps.setInt(2, start);
						},
						(rs, rownum) -> {
							return new UserAccountDto(rs.getString("id"), rs
									.getString("password"), rs
									.getString("firstname"), rs
									.getString("lastname"), rs.getString("email"),
									rs.getInt("office_id"));
						});
			} else {
				return template.query(
					FETCH_BY_OFFICE,
					ps -> {
						ps.setInt(1, officeId);
						ps.setInt(2, size);
						ps.setInt(3, start);
					},
					(rs, rownum) -> {
						return new UserAccountDto(rs.getString("id"), rs
								.getString("password"), rs
								.getString("firstname"), rs
								.getString("lastname"), rs.getString("email"),
								rs.getInt("office_id"));
					});
			}
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCount(int officeId) throws IOException {
		try {
			if (officeId <= 0) {
				return template.queryForObject(USER_COUNT,
						(rs, rownum) -> {
							return rs.getInt(1);
						});
			} else {
				return template.queryForObject(USER_COUNT_BY_OFFICE,
						(rs, rownum) -> {
							return rs.getInt(1);
						}, officeId);
			}
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void insert(UserAccountDto userAccount) throws IOException {
		try {
			template.update(INSERT_USER, (ps) -> {
				ps.setString(1, userAccount.getId());
				ps.setString(2, userAccount.getPassword());
				ps.setString(3, userAccount.getFirstName());
				ps.setString(4, userAccount.getLastName());
				ps.setString(5, userAccount.getEmail());
				ps.setInt(6, userAccount.getRegionId());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void update(UserAccountDto userAccount)
			throws IOException {
		try {
			if (userAccount.getPassword() == null) {
				template.update(UPDATE_USER, (ps) -> {
					ps.setString(1, userAccount.getFirstName());
					ps.setString(2, userAccount.getLastName());
					ps.setString(3, userAccount.getEmail());
					ps.setInt(4, userAccount.getRegionId());
					ps.setString(5, userAccount.getId());
				});
			} else {
				template.update(UPDATE_USER_AND_PASSWORD, (ps) -> {
					ps.setString(1, userAccount.getPassword());
					ps.setString(2, userAccount.getFirstName());
					ps.setString(3, userAccount.getLastName());
					ps.setString(4, userAccount.getEmail());
					ps.setInt(5, userAccount.getRegionId());
					ps.setString(6, userAccount.getId());
				});
			}
			
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deleteBy(String id) throws IOException {
		try {
			template.update(DELETE_USER, (ps) -> ps.setString(1, id));
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getLanguage(String id) throws IOException {
		try {
			return template.queryForObject(GET_USER_LANGUAGE, (rs, rownum) -> {
				return rs.getString(1);
			}, id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateLanguage(String id, String language)
			throws IOException {
		try {
			template.update(UPDATE_USER_LANGUAGE, ps -> {
				ps.setString(1, language);
				ps.setString(2, id);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
}
