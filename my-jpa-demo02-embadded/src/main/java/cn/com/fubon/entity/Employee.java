package cn.com.fubon.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee extends AbstractEntity{
	private String name;
	
	/**
	 * 注解@JoinColumn应该是放在关系的维护方，即包含联接列的实体中
	 */
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="depart_id")
	private Department departt;
	
	/* 枚举MYSQL默认产生INT类型字段
	 * 对于FULL_TIME_EMPLOYEE("full_time")，入库是FULL_TIME_EMPLOYEE，重写了toString也没有用 */
	@Enumerated(EnumType.STRING)
	private EmployeeType type;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="emp_phone",joinColumns={@JoinColumn(name="emp_id")},
			inverseJoinColumns={@JoinColumn(name="phone_id")})
	private Set<Phone> phones;
	
	@Embedded
	private Address address;
}
