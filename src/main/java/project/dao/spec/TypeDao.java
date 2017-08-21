package project.dao.spec;

import java.io.IOException;
import java.util.List;

import project.dto.TypeDto;

public interface TypeDao {
	void insertNewType (String typeName) throws IOException;
	int getTotalCount () throws IOException;
	TypeDto getBy (int typeId) throws IOException;
	List <TypeDto> getAll (int start, int size) throws IOException;
}
