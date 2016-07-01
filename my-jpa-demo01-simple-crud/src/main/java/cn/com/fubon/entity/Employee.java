package cn.com.fubon.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
//@Setter
public class Employee extends AbstractEntity{
	
	/* 不指定注解或者注解不指定EnumType的枚举属性在MYSQL会默认产生INT类型字段。
	 * 对于FULL_TIME_EMPLOYEE("full_time")，入库是FULL_TIME_EMPLOYEE，重写了toString也没有用 */
	@Enumerated(EnumType.STRING)
	private EmployeeType type;

	public void setType(EmployeeType type) {
		this.type = type;
	}
	
	
}
