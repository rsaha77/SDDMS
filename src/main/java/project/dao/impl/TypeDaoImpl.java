package project.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.TypeDao;
import project.dto.TypeDto;

@Repository
public class TypeDaoImpl implements TypeDao {
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public void insertNewType (String typeName) throws IOException {
		try {
			template.update (
				"Insert into TYPE (typeName) VALUES (?)",
				(ps) -> {
					ps.setString (1, typeName);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public int getTotalCount() throws IOException {
		return template.queryForObject (
			"Select count(*) from type",
			(rs, rownum) -> {
				return rs.getInt (1);
			}
		);
	}
	
	@Override
	public TypeDto getBy (int typeId) throws IOException {
		try {
			return template.queryForObject (
				"Select * from type where typeId = ?",
				(rs, rownum) -> {
					return new TypeDto (rs.getInt ("typeId"), rs.getString ("typeName"));
				},
				typeId
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public List <TypeDto> getAll (int start, int size) throws IOException {
		try {
			return template.query (
				"SELECT * FROM type LIMIT ? OFFSET ?",
				ps -> {
					ps.setInt (1, size);
					ps.setInt (2, start);
				},
				(rs, rownum) -> {
					return new TypeDto (rs.getInt ("typeId"), rs.getString ("typeName"));
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
}
