package cn.com.fubon.entity;

import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * @MappedSuperclass 
 * as superclasse that contain persistent state and mapping information,but are not entities.
 */
@MappedSuperclass
@Getter
@Setter
public class Product extends AbstractEntity{
	private String name;
}
