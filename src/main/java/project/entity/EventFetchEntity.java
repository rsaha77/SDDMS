package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventFetchEntity {
	private int id;
	private String uid; // group code or uid
	private String title;
	private String type;
	private String dt1;
	private String dt2;
	private String location;
	private String byl;
	private int eid;
}
