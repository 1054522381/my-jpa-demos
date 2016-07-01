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
	
	private String name;
	
	/**
	 * 单向一对多，如果没有用@JoinColumn，会产生一张中间表，记录两个实体的id映射
	 * mappedBy 应该放在关系被维护方
	 */
	@OneToMany(cascade=CascadeType.PERSIST,mappedBy="departt")
	//@JoinColumn(name="depart_id")
	private Set<Employee> emps;
}
