package project.entity;

import java.util.List;

import project.dto.RegionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionEntity {
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List <RegionDto> regions;
}
