package cn.com.fubon.entity;


import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="t_group")
@Cacheable(value=true)
@Setter
@Getter
public class Group extends AbstractEntity{
	private String name;
	
	/**
	 * 如果不指定@MapKey，默认会在t_user产生users_KEY字段
	 */
	@OneToMany(mappedBy="group", cascade=CascadeType.PERSIST)
	@MapKey(name="id")
	private Map<Long, User> users = new HashMap<Long, User>();
	
}
