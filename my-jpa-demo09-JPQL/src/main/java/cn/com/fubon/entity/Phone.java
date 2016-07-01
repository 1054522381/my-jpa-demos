package cn.com.fubon.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Cacheable(true)
@Getter
@Setter
public class Phone extends AbstractEntity{
	private String type;
	
	private String number;
	
}
