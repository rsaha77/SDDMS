package project.dao.impl;

import java.io.IOException;

import static java.lang.System.out;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.dto.ClientDetailsDto;
import project.dao.spec.CalendarDao;
import project.dao.spec.ClientDao;
import project.dao.spec.RecentActivityDao;
import project.entity.ClientDetails;
import project.entity.ClientDetailsEntity;
import project.entity.ClientImportantNoteCreationEntity;
import project.entity.ClientLeadDetailsEntity;
import project.entity.ClientModifyCreationEntity;
import project.entity.DisqualifiedLeadDetailsListEntity;
import project.entity.DisqualifiedLeadEntity;
import project.entity.LeadDetailsEntity;
import project.entity.LeadDetailsListEntity;

@Repository
public class ClientDaoImpl implements ClientDao {
	
	@Autowired
	private RecentActivityDao recentActivityDao;
	
	@Autowired
	private CalendarDao calendarDao;

	@Autowired
	private JdbcTemplate template;

	private static final String FETCH_BY_ID = "SELECT * FROM client WHERE id = ?";

	private static final String FETCH = "SELECT * FROM client";
	
	private static final String CLIENT_COUNT = "SELECT COUNT(*) FROM client";
	
	private static final String INSERT_CLIENT = "INSERT INTO CLIENT "
			+ " (id, email, name, type, address, contact, transport, next_order_date, important_note, region)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	private static final String UPDATE_CLIENT = 
			"UPDATE CLIENT SET name = ?, email = ?, address = ?, contact = ?, next_order_date = ? WHERE id = ?";
	
	Calendar cal = Calendar.getInstance();
	
	public int min (int a, int b) {
		return (a < b ? a : b);
	}
	
	@Override
	public List <ClientDetailsDto> getBy (int id, int start, int size) throws IOException {
		try {
			if (id <= 0) {
				return template.query (
					FETCH,
					ps -> {
					},
					(rs, rownum) -> {
						return new ClientDetailsDto (
							rs.getString ("id"),
							rs.getString ("name"),
							rs.getString  ("email"),
							rs.getString ("type"),
							rs.getString ("address"),
							rs.getString ("contact"),
							rs.getString ("transport"),
							rs.getString ("next_Order_Date"),
							rs.getString ("important_Note"),
							rs.getString ("region")
						);
					}
				);
			} else {
				return template.query (
					FETCH_BY_ID,
					ps -> {
						ps.setInt (1, id);
						ps.setInt (2, size);
						ps.setInt (3, start);
					},
					(rs, rownum) -> {
						return new ClientDetailsDto (
							rs.getString ("id"), 
							rs.getString ("name"), 
							rs.getString  ("email"),
							rs.getString ("type"),
							rs.getString ("address"),
							rs.getString ("contact"),
							rs.getString ("transport"),
							rs.getString ("next_Order_Date"),
							rs.getString ("important_Note"),
							rs.getString ("region")
						);
					}
				);
			}
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCount(int id) throws IOException {
		try {
			return template.queryForObject (
				CLIENT_COUNT,
				(rs, rownum) -> {
					return rs.getInt(1);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	public String getInstance () {
		DateFormat df = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
		java.util.Date today = Calendar.getInstance().getTime();   
		return df.format(today);
	}

	
	public String getClientNameFromClientTable (String clientId) {
		return template.queryForObject (
			"SELECT * from client WHERE id = ?",
			(rs, rownum) -> {return rs.getString ("name");},
			clientId
		);
	}
	
	
	@Override
	public void insertNewClient (ClientDetailsDto clientDetails) throws IOException {
		System.out.println ("====>>>> " + clientDetails);
		try {
			template.update (INSERT_CLIENT, (ps) -> {
				ps.setString (1, clientDetails.getClientId ());
				ps.setString (2, clientDetails.getEmail ());
				ps.setString (3, clientDetails.getName ());
				ps.setString (4, clientDetails.getType ());
				ps.setString (5, clientDetails.getAddress());
				ps.setString (6, clientDetails.getContact());
				ps.setString (7, clientDetails.getTransport());
				ps.setString (8, clientDetails.getNextOrderDate());
				ps.setString (9, clientDetails.getImportantNote());
				ps.setString (10, clientDetails.getRegion());
			});
		} catch (DataAccessException e) {
			System.out.println ("Cannot Insert Client : " + clientDetails.getClientId());
			throw new IOException(e);
		}
		recentActivityDao.insert ("Added", "a new client (" + getClientNameFromClientTable (clientDetails.getClientId ()) + ", " + clientDetails.getClientId ()+ ") ", getInstance ());
	}
	
	@Override
	public void update (ClientModifyCreationEntity clientModifyCreationEntity) throws IOException {
		try {
			template.update (
				UPDATE_CLIENT,
				(ps) -> {
					ps.setString (1, clientModifyCreationEntity.getName ());
					ps.setString (2, clientModifyCreationEntity.getEmail ());
					ps.setString (3, clientModifyCreationEntity.getAddress());
					ps.setString (4, clientModifyCreationEntity.getContact());
					ps.setString (5, clientModifyCreationEntity.getNextOrderDate());
					ps.setString (6, clientModifyCreationEntity.getClientId());
				}
			);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		recentActivityDao.insert ("Updated", "information for the client (" + clientModifyCreationEntity.getName () + ", " + clientModifyCreationEntity.getClientId ()+ ") ", getInstance ());
	}
	
	public String getPreviousImportantNote (String clientId) {
		out.println ("clientId : " + clientId);
		return template.queryForObject (
			"SELECT * from client WHERE id = ?",
			(rs, rownum) -> {return rs.getString ("important_note");},
			clientId
		);
	}
	
	@Override
	public void updateImportantNote (ClientImportantNoteCreationEntity clientImportantNoteCreationEntity) throws IOException {
		String clientId = clientImportantNoteCreationEntity.getClid ();
		String newNote = getPreviousImportantNote (clientId) + "<br> " + clientImportantNoteCreationEntity.getImportantNote ();
		try {
			template.update (
				"UPDATE client SET important_note = ? WHERE id = ?",
				(ps) -> {
					ps.setString (1, newNote);
					ps.setString (2, clientId);
				}
			);
				
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
		recentActivityDao.insert ("Updated", "important note for the client (" + getClientNameFromClientTable (clientId) + ", " + clientId + ")", getInstance ());
	}
	
	@Override
	public void deleteBy(String id) throws IOException {
		String clientName = getClientNameFromClientTable (id);
		try {
			template.update (
				"DELETE FROM CLIENT WHERE id = ?",
				(ps) -> ps.setString(1, id)
			);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		recentActivityDao.insert ("Deleted", "the client (" + clientName + ", " + id + ")", getInstance ());
	}
	
	
	public String getClientIdFromOrderTableByOrderId (int orderId) {
		return template.queryForObject (
			"Select * from orders where orderId = ?",
			(rs, rownum) -> {
				return new String (rs.getString ("clientId"));
			},
			orderId
		);
	}
	
	@Override
	public ClientDetailsEntity getClientDetailsForClientId (String clientId) {
		out.println ("clientId: " + clientId + "\n");
		return template.queryForObject (
			"SELECT * from client WHERE id = ?",
			(rs, rownum) -> {
				return new ClientDetailsEntity (
					clientId,
					rs.getString ("name"), 
					rs.getString  ("email"),
					rs.getString ("type"),
					rs.getString ("address"),
					rs.getString ("contact"),
					rs.getString ("transport"),
					rs.getString ("next_Order_Date"),
					rs.getString ("important_Note"),
					rs.getString ("region")
				);
			},
			clientId
		);
	}
	
	
	
	
	@Override
	public DisqualifiedLeadEntity getDisqForClientId (String clientId) {
		return template.queryForObject (
			"SELECT * from disqualifiedLeads WHERE clientId = ?",
			(rs, rownum) -> {
				return new DisqualifiedLeadEntity (
					clientId,
					rs.getString ("disLeadName"), 
					rs.getString ("disLeadContact"),
					rs.getString  ("disLeadEmail"),
					rs.getString ("disLeadAddress"),
					rs.getString ("disLeadType"),
					rs.getString ("disLeadTransport"),
					rs.getString ("disLeadRegion")
				);
			},
			clientId
		);
	}
	
	
	@Override
	public ClientDetailsEntity getClientDetailsForOrderId (int orderId) {
		out.println ("orderId: " + orderId + "\n");
		String clientId = getClientIdFromOrderTableByOrderId (orderId);
		return getClientDetailsForClientId(clientId);
	}
	
	
	/*
	 * 
	 * @Override
	public List <ClientDetailsDto> getBy (int id, int start, int size) throws IOException {
		try {
			if (id <= 0) {
				return template.query (
					FETCH,
					ps -> {
					},
					(rs, rownum) -> {
						return new ClientDetailsDto (
							rs.getString ("id"),
							rs.getString ("name"), 
							rs.getString  ("email"),
							rs.getString ("type"),
							rs.getString ("address"),
							rs.getString ("contact"),
							rs.getString ("transport"),
							rs.getString ("next_Order_Date"),
							rs.getString ("important_Note"),
							rs.getString ("region")
						);
					}
				);
			} else {
				return template.query (
					FETCH_BY_ID,
					ps -> {
						ps.setInt (1, id);
						ps.setInt (2, size);
						ps.setInt (3, start);
					},
					(rs, rownum) -> {
						return new ClientDetailsDto (
							rs.getString ("id"), 
							rs.getString ("name"), 
							rs.getString  ("email"),
							rs.getString ("type"),
							rs.getString ("address"),
							rs.getString ("contact"),
							rs.getString ("transport"),
							rs.getString ("next_Order_Date"),
							rs.getString ("important_Note"),
							rs.getString ("region")
						);
					}
				);
			}
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	 * 
	 */
	
	
	
	public String  getLabelledReminder (String status) {
		String ret = "<span class='label label-default'>"  +  "NO ACTIVITY" + "</span>";
		if (status.compareTo ("TRUE") == 0) {
			ret = "<span class='label label-success'>" + "<span class='glyphicon glyphicon-ok'></span>" +  " REMINDER SET" + "</span>";
		}
		return ret;
	}
	
	public List <LeadDetailsEntity> getLeadsFromLeadTable () {
		return template.query (
			"SELECT * from lead",
			ps -> {
				
			},
			(rs, rownum) -> {
				String reminderSetLabelled = getLabelledReminder (rs.getString  ("reminderSet"));
				return new  LeadDetailsEntity (
					rs.getString ("clientId"),
					rs.getString ("leadName"), 
					rs.getString  ("leadContact"),
					rs.getString  ("leadEmail"),
					rs.getString  ("leadAddress"),
					rs.getString  ("leadType"),
					rs.getString  ("leadTransport"),
					rs.getString  ("leadRegion"),
					reminderSetLabelled
				);
			}
		);
	}
	
	public List <LeadDetailsEntity> getLeadsFromLeadTableLimitedTo5 () {
		return template.query (
			"SELECT * from lead WHERE reminderSet = ? LIMIT 5",
			ps -> {
				ps.setString (1, "FALSE");
			},
			(rs, rownum) -> {
				return new  LeadDetailsEntity (
					rs.getString ("clientId"),
					rs.getString ("leadName"), 
					rs.getString  ("leadContact"),
					rs.getString  ("leadEmail"),
					rs.getString  ("leadAddress"),
					rs.getString  ("leadType"),
					rs.getString  ("leadTransport"),
					rs.getString  ("leadRegion"),
					rs.getString  ("reminderSet")
				);
			}
		);
	}
	
	
	
	@Override
	public LeadDetailsListEntity getLeadDetailsList () {
		List <LeadDetailsEntity> leadList = getLeadsFromLeadTableLimitedTo5 ();
		int leadCount = leadList.size ();
		leadCount = min (leadCount, 7);
		return new LeadDetailsListEntity (0, leadCount, 10, leadList);
	}
	
	
	@Override
	public LeadDetailsListEntity getLeadDetailsListAll () {
		List <LeadDetailsEntity> leadList = getLeadsFromLeadTable ();
		return new LeadDetailsListEntity (0, 0, 10, leadList);
	}
	
	public List <DisqualifiedLeadEntity> getDisLeadFromDisLeadTable () {
		return template.query (
			"SELECT * from disqualifiedLeads",
			(rs, rownum) -> {
				return new DisqualifiedLeadEntity(
					rs.getString ("clientId"),
					rs.getString ("disLeadName"),
					rs.getString ("disLeadContact"),
					rs.getString ("disLeadEmail"),
					rs.getString ("disLeadAddress"),
					rs.getString ("disLeadType"),
					rs.getString ("disLeadTransport"),
					rs.getString ("disLeadRegion")
				);
			}
		);
	}
	
	
	@Override
	public DisqualifiedLeadDetailsListEntity getDisqualifiedLeadDetailsListAll () {
		List <DisqualifiedLeadEntity> disLeadList = getDisLeadFromDisLeadTable ();
		return new DisqualifiedLeadDetailsListEntity (0, 0, 10, disLeadList);
	}
	
	
	public LeadDetailsEntity getLeadFromLeadTableForLeadId (String clientId) {
		return template.queryForObject (
			"Select * from lead where clientId = ?",
			(rs, rownum) -> {
				return new  LeadDetailsEntity (
					clientId,
					rs.getString ("leadName"), 
					rs.getString  ("leadContact"),
					rs.getString  ("leadEmail"),
					rs.getString  ("leadAddress"),
					rs.getString  ("leadType"),
					rs.getString  ("leadTransport"),
					rs.getString  ("leadRegion"),
					rs.getString  ("reminderSet")
				);
			},
			clientId
		);
			
	}
	
	@Override
	public LeadDetailsEntity getLeadDetailsForLeadId (String clientId) {
		return getLeadFromLeadTableForLeadId (clientId);
	}
	
	@Override
	public int getLeadCountFromLeadTable () {
		int x = template.queryForObject (
			"Select count(*) from lead where reminderSet = ?",
			(rs, rownum) -> {
				return rs.getInt(1);
			},
			"FALSE"
		);
		return (x <= 5 ? x : 5);
	}
	
	@Override
	public List <LeadDetailsEntity> getLeadDetailsList2 () {
		return getLeadsFromLeadTable ();
	}
	
	
	@Override
	public List <ClientDetails> getClientDetailsList () throws IOException {
		try {
			return template.query (
				"Select * from client",
				ps -> {
				},
				(rs, rownum) -> {
					return new ClientDetails (
						rs.getString("id"),
						rs.getString("name")
					);
				}
			);
		} catch (DataAccessException e) {
			throw new IOException (e);
		}
	}
	
	
	@Override
	public List <ClientLeadDetailsEntity> getClientLeadDetailsList () {
	
		List <LeadDetailsEntity> obLead = getLeadsFromLeadTable ();
		List <ClientDetails> obClient = new ArrayList <ClientDetails>();
		List <ClientLeadDetailsEntity> ret = new ArrayList <ClientLeadDetailsEntity>();
		
		try {
			obClient = getClientDetailsList ();
		} catch (IOException e) {
			out.println (e);
		}
		for (LeadDetailsEntity ob: obLead) {
			ret.add (new ClientLeadDetailsEntity (ob.getClientId (), ob.getLeadName ()));
		}
		for (ClientDetails ob: obClient) {
			ret.add (new ClientLeadDetailsEntity (ob.getClientId (), ob.getClientName ()));
		}
		return ret;
	}
	
	
	public int clientCountInClientTable (String clientId) {
		return template.queryForObject (
			"Select count(*) from client where id like ?",
			(rs, rownum) -> {
				return rs.getInt(1);
			},
			clientId
		);
	}
	
	
	public boolean clientIdExistsInClientTable (String clientId) {
		int temp = clientCountInClientTable (clientId);
		System.out.println ("temp : " + temp);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public boolean isLead (String id) {
		if ( clientIdExistsInClientTable (id)) {
			return false;
		} else {
			return true;
		}
	}
	
	
	private static final String INSERT_INTO_DISQUALIFIED_LEADS = "INSERT INTO disqualifiedLeads "
			+ " (clientId, disLeadName, disLeadContact, disLeadEmail, disLeadAddress, disLeadType, disLeadTransport, disLeadRegion)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	public void insertLeadIntoDisqualified (String clientId) {
		LeadDetailsEntity leadDetailsEntity = getLeadFromLeadTableForLeadId (clientId);
		template.update (
			INSERT_INTO_DISQUALIFIED_LEADS,
			(ps) -> {
				ps.setString (1, leadDetailsEntity.getClientId ());
				ps.setString (2, leadDetailsEntity.getLeadName());
				ps.setString (3, leadDetailsEntity.getLeadContact());
				ps.setString (4, leadDetailsEntity.getLeadEmail());
				ps.setString (5, leadDetailsEntity.getLeadAddress());
				ps.setString (6, leadDetailsEntity.getLeadType());
				ps.setString (7, leadDetailsEntity.getLeadTransport());
				ps.setString (8, leadDetailsEntity.getLeadRegion());
			}
		);
	}
	
	public void deleteLeadFromLeadTable (String clientId) {
		template.update (
			"DELETE from lead where clientId = ?",
			(ps) -> {
				ps.setString (1, clientId);
			}
		);
	}
	
	public void deleteFromCalendarEventsWhereTitleIs (String title) {
		template.update (
			"DELETE from calendarEvents where title = ?",
			(ps) -> {
				ps.setString (1, title);
			}
		);
	}
	
	
	@Override
	public void disqualifyLead (String clientId) {
		// Insert into disqualified list first
		insertLeadIntoDisqualified (clientId);
		// Delete from leads
		deleteLeadFromLeadTable (clientId);
		// Delete from calendarEvents
		deleteFromCalendarEventsWhereTitleIs ("Contact Lead " + clientId);
	}
	
	@Override
	public void setReminderTrueForLeadId (String clientId) {
		template.update (
			"UPDATE lead SET reminderSet = ? WHERE clientId = ?",
			(ps) -> {
				ps.setString (1, "TRUE");
				ps.setString (2, clientId);
			}
		);
	}
	
	
	private static final String INSERT_LEAD = "INSERT INTO LEAD "
			+ " (clientId, leadName, leadContact, leadEmail, leadAddress, leadType, leadTransport, leadRegion)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	@Override
	public void insertNewLead (LeadDetailsEntity leadDetailsEntity) {
		template.update (
			INSERT_LEAD,
			(ps) -> {
				ps.setString (1, leadDetailsEntity.getClientId());
				ps.setString (2, leadDetailsEntity.getLeadName());
				ps.setString (3, leadDetailsEntity.getLeadContact());
				ps.setString (4, leadDetailsEntity.getLeadEmail());
				ps.setString (5, leadDetailsEntity.getLeadAddress());
				ps.setString (6, leadDetailsEntity.getLeadType());
				ps.setString (7, leadDetailsEntity.getLeadTransport());
				ps.setString (8, leadDetailsEntity.getLeadRegion());
			}
		);
	}
	
	
	@Override
	public void deleteLead (String clientId) {
		insertLeadIntoDisqualified (clientId);
		template.update (
			"DELETE FROM lead where clientId = ?",
			(ps) -> {
				ps.setString (1, clientId);
			}
		);
	}
	
	@Override
	public void modifyLead (LeadDetailsEntity leadDetailsEntity) {
		template.update (
			"UPDATE lead SET leadName = ?, leadEmail = ?, leadType = ?, leadAddress = ?, leadContact = ?, leadRegion = ?, leadTransport = ? WHERE clientId = ?",
			(ps) -> {
				ps.setString (1, leadDetailsEntity.getLeadName ());
				ps.setString (2, leadDetailsEntity.getLeadEmail ());
				ps.setString (3, leadDetailsEntity.getLeadType ());
				ps.setString (4, leadDetailsEntity.getLeadAddress ());
				ps.setString (5, leadDetailsEntity.getLeadContact ());
				ps.setString (6, leadDetailsEntity.getLeadRegion ());
				ps.setString (7, leadDetailsEntity.getLeadTransport ());
				ps.setString (8, leadDetailsEntity.getClientId ());
			}
		);
	}
	
	@Override
	public void deleteDisq (String clientId) {
		template.update (
			"DELETE FROM disqualifiedLeads WHERE clientId = ?",
			(ps) -> {
				ps.setString (1, clientId);
			}
		);
	}
}



































