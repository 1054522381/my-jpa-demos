package cn.com.fubon.entity;

import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ptype",discriminatorType=DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Project extends AbstractEntity{
	private String name;
	
	@ManyToMany(mappedBy="projects")
	private Set<Employee> employees;
}
