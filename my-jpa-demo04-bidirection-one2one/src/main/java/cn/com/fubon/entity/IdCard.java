package cn.com.fubon.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
	
	/**
	 * 如果不指定mappedby会在两张表产生外键。
	 */
	@OneToOne(mappedBy="card",cascade=CascadeType.PERSIST)
	private Person person;
}
