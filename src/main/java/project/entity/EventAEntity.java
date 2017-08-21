package project.entity;

import java.util.List;





import project.dto.EventADto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAEntity {
	List<EventADto> list;
}
