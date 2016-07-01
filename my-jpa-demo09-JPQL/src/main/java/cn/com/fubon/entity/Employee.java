package cn.com.fubon.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NamedQueries({
	@NamedQuery(name="Employee.findAll",query="select e from Employee e"),
	@NamedQuery(name="Employee.findByName",query="select e from Employee e where e.name=:name"),
	/*甚至可以用实体作为参数*/
	@NamedQuery(name="Employee.findByDept",query="select e from Employee e where e.departt=:dept")
})
public class Employee extends AbstractEntity{
	public Employee(){
		
	}
	
	private String name;
	
	/**
	 * 注解@JoinColumn应该是放在关系的维护方，即包含联接列的实体中
	 */
	@ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
	@JoinColumn(name="depart_id")
	private Department departt;
	
	/* 枚举MYSQL默认产生INT类型字段
	 * 对于FULL_TIME_EMPLOYEE("full_time")，入库是FULL_TIME_EMPLOYEE，重写了toString也没有用 */
	@Enumerated(EnumType.STRING)
	private EmployeeType type;
	
	@OneToMany(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
	@JoinTable(name="emp_phone",joinColumns={@JoinColumn(name="emp_id")},
			inverseJoinColumns={@JoinColumn(name="phone_id")})
	private Set<Phone> phones;
	
	@Embedded
	private Address address;
	
	@Temporal(TemporalType.DATE)
	private Date birthDay;
	
	private BigDecimal salary;
}
