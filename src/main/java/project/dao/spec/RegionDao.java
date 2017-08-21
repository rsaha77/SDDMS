package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.dto.RegionDto;

public interface RegionDao {
	void insertNewRegion (String regionName) throws IOException;
	int getTotalCount () throws IOException;
	RegionDto getBy (int regionId) throws IOException;
	List <RegionDto> getAll (int start, int size) throws IOException;
}