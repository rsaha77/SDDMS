package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChartEntity {
	private int fast;
	private int health;
	private int work;
	private int sports;
	private int movie;
}
