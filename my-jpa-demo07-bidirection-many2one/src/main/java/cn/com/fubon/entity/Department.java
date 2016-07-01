package cn.com.fubon.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Department extends AbstractEntity{
	
	private String dname;
	
	/**
	 * mappedBy指定对方为关系的维护方，双向关系如果不指定关系的维护方，会有外键，同时会产生一张映射表。
	 */
	@OneToMany(mappedBy="department",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private Set<Employee> emps;
}
