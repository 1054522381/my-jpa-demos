package cn.com.fubon.entity;

import java.util.Collection;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee extends AbstractEntity{
	private String name;
	
	//如果没有指定集合泛型，可以指定targetClass
	@ElementCollection/*(targetClass=VacationEntry.class)*/ 
	@CollectionTable(name="vacation")
	@AttributeOverride(name="daysTaken",column=@Column(name="daysTaken"))
	private Collection<Vacation> vacations;
	
	/*
	 * 默认集合表名为"实体名_集合属性名"，联接列默认是"实体名_实体主键"
	 */
	@ElementCollection
	@CollectionTable(name="employee_nicknames",joinColumns={
			@JoinColumn(name="emp_id")
	})
	@Column(name="nickName")
	private Set<String> nickNames;
}
