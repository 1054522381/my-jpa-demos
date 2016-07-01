package cn.com.fubon.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class IdCard extends AbstractEntity{
	
	private String number;
	
	@Temporal(TemporalType.DATE)
	private Date expireDate;
}
