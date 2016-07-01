package cn.com.fubon.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student extends AbstractEntity{
	
	private String sname;
	
	/*
	 * 中间表默认产生的字段名是属性名_id，如teachers_id,students_id
	 */
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="t_student_teacher",
			joinColumns={@JoinColumn(name="student_id",referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="teacher_id",referencedColumnName="id")})
	private Set<Teacher> teachers;
}
