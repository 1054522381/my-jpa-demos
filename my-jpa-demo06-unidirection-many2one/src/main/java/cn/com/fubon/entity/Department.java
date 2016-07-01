package cn.com.fubon.entity;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Department extends AbstractEntity{
	
	private String dname;
}
