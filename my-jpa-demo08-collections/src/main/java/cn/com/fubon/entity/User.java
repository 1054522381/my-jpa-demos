package cn.com.fubon.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="t_user")
@Setter
@Getter
@ToString
public class User extends AbstractEntity{
	private String name;
	
	@ManyToOne(cascade={CascadeType.ALL})
	private Group group;
}