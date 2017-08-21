package project.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.RegionDao;
import project.dto.RegionDto;

@Repository
public class RegionDaoImpl implements RegionDao {

	@Autowired
	private JdbcTemplate template;

	@Override
	public void insertNewRegion (String regionName) throws IOException {
		try {
			template.update (
				"Insert into regions (regionName) values (?)",
				(ps) -> {
					ps.setString (1, regionName);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public int getTotalCount() throws IOException {
		return template.queryForObject (
			"Select count(*) from regions",
			(rs, rownum) -> {
				return rs.getInt (1);
			}
		);
	}
	
	@Override
	public RegionDto getBy (int regionId) throws IOException {
		try {
			return template.queryForObject (
				"Select * from regions where regionId = ?",
				(rs, rownum) -> {
					return new RegionDto (rs.getInt ("regionId"), rs.getString ("regionName"));
				},
				regionId
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public List <RegionDto> getAll (int start, int size) throws IOException {
		try {
			return template.query (
				"SELECT * FROM regions LIMIT ? OFFSET ?",
				ps -> {
					ps.setInt (1, size);
					ps.setInt (2, start);
				},
				(rs, rownum) -> {
					return new RegionDto (rs.getInt ("regionId"), rs.getString ("regionName"));
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
}







