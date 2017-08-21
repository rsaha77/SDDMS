package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.entity.RecentActivityEntity;

public interface RecentActivityDao {
	List <RecentActivityEntity> getRecentActivityList () throws IOException;
	
	void insert (String status, String details, String instance) throws IOException;
}
