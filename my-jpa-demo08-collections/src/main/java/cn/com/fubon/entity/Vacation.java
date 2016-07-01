package cn.com.fubon.entity;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Vacation {
	private Calendar startDate;
	
	@Column(name="days")
	private Integer daysTaken;
}
