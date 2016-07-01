package cn.com.fubon.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Department extends AbstractEntity{
	
	private String dname;
	
	/*默认产生一张id映射表记录关联关系*/
	@OneToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	//@JoinTable(name="dept_emp") //可以指定默认产生的映射表的表名
	@JoinColumn(name="dept_id",referencedColumnName="id") //可以不用中间表，指定多方引用外键
	private Set<Employee> employees;
}
