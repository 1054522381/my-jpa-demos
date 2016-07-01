package cn.com.fubon.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto{
	public EmployeeDto(){
		
	}
	
	public EmployeeDto(String name,Date birthDay){
		this.name = name;
		this.birthDay = birthDay;
	}
	
	private String name;
	
	private Date birthDay;
	
}
