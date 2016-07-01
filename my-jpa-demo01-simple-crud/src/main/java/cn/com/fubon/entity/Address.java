package cn.com.fubon.entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

/*
 * @Entity(name="addr")指定该类为JPA实体
 * @Table(name="t_address")指定表名，如果没有指定，默认表名为 Entity name
 */
@Entity(name="addr")
@Table(name="t_address",uniqueConstraints={@UniqueConstraint(columnNames = { "city","street" })})
@Getter
@Setter
public class Address extends AbstractEntity {
	private String street;
	
	//columnDefinition可以指定具体的数据库类型
	@Column(columnDefinition="varchar(50) comment '城市'")
	private String city;
	
	@Column(nullable=false)
	private String country;
	
	//@Transient
	/*  java.util.Date
		java.sql.Timestamp
		java.util.Calendar
	 *  MYSQL默认产生DATETIME类型的字段 */
	@Temporal(TemporalType.DATE)
	private Date createTime;
	private Timestamp ts;
	private Calendar cld;
	
	/* java.sql.Time MYSQL默认产生TIME类型的字段 */
	private Time time;
	
	@Override
	public String toString() {
		return "id=" + this.getId() + ";street=" + street + ";city=" + city + ";country=" + country
				+ ";createTime=" + createTime + ";ts=" + ts + ";cld=" + cld
				+ ";time=" + time;
	}
}
