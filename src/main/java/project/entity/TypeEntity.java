package project.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dto.TypeDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeEntity {
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List <TypeDto> types;
}
