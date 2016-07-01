package cn.com.fubon.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;
/**
 * InheritanceType.SINGLE_TABLE: 默认 
 * 用字段区分子类类型
 * 
 * InheritanceType.JOINED:
 * 子类的字段会单独产生表，id是父表的外键
 * 
 * InheritanceType.TABLE_PER_CLASS:
 * This strategy provides poor support for polymorphic relationships, and usually requires either
 * SQL UNION queries or separate SQL queries for each subclass for queries that cover the entire
 * entity class hierarchy.
 * Support for this strategy is optional, and may not be supported by all Java Persistence API
 * providers. The default Java Persistence API provider in the Application Server does not support
 * this strategy.
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ptype",discriminatorType=DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Project extends AbstractEntity{
	private String name;
}
