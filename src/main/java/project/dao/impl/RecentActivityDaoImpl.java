package project.dao.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dao.spec.RecentActivityDao;
import project.entity.RecentActivityEntity;

@Repository
public class RecentActivityDaoImpl implements RecentActivityDao{
	
	@Autowired
	private JdbcTemplate template;
	
	/*
	 * default primary success info warning danger
	 */
	
	public String getLabelType (String status) {
		String label = "info";
		if (status.equals ("Cancelled")) {
			label = "danger";
		} else if (status.equals ("Processed") || status.equals ("Placed") || status.equals ("Reported")) {
			label = "success";
		} else if (status.equals ("Deleted") || status.equals("Updated")) {
			label = "warning";
		}
		return label;
	}

	public String getLabelledStatus (String status) {
		String front = "<span class='label label-";
		String labelType = getLabelType (status);
		String mid = "'>";
		String end = "</span>";
		return front + labelType + mid + status + end;
	}
	
	
	public String getLabelledInstance (String instance) {
		return "<span class='label label-default'>" + instance + "</span>";
	}
	
	@Override
	public List <RecentActivityEntity> getRecentActivityList () throws IOException {
		try {
			return template.query (
				"Select * from recentActivity order by instance desc LIMIT 15",
				ps -> {
				},
				(rs, rownum) -> {
					return new RecentActivityEntity (
						getLabelledStatus (rs.getString ("status")),
						rs.getString ("details"),
						getLabelledInstance (rs.getString ("instance"))
					);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	@Override
	public void insert (String status, String details, String instance) throws IOException {
		try {
			template.update (
				"INSERT INTO recentActivity (status, details, instance) VALUES (?, ?, ?)",
				(ps) -> {
					ps.setString (1, status);
					ps.setString (2, details);
					ps.setString (3, instance);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
}