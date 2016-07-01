package cn.com.fubon.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/*
 * @MappedSuperclass 表示当前不是受管理的实体类，会被其他实体类继承
 * 注：被@Entity或者@MappedSuperclass标识的类需要指定主键
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@Column(name="_Id") //可以指定主键字段名
	private Long id;
	
	/*
	 * @Id指定主键字段，如果没有用@Column指定字段名，字段名默认为属性名;
	 * 主键生成策略：
	 * IDENTITY：使用数据库的自增长字段，需要数据库的支持（如MySQL自增），以下是测试产生的SQL：
	 * 
	    Hibernate: 
		    drop table if exists t_address
		Hibernate: 
		    create table t_address (
		        id bigint not null auto_increment,
		        city varchar(255),
		        country varchar(255),
		        street varchar(255),
		        primary key (id)
		    )
	 * 
	 * AUTO：由JPA自动生成，以下是测试产生的SQL：
	 * 
	    Hibernate: 
		    drop table if exists hibernate_sequence
		Hibernate: 
		    drop table if exists t_address
		Hibernate: 
		    create table hibernate_sequence (
		        next_val bigint
		    )
		Hibernate: 
		    insert into hibernate_sequence values ( 1 )
		Hibernate: 
		    create table t_address (
		        id bigint not null,
		        city varchar(255),
		        country varchar(255),
		        street varchar(255),
		        primary key (id)
		    )
	 * 
	 * SEQUENCE：使用数据库的序列号，需要数据库的支持（如Oracle）
	 * TABLE：
	 * 使用指定的数据库表记录ID的增长，需要定义一个TableGenerator，在@GeneratedValue中引用。例如：
	 * @TableGenerator(name="myGenerator",table="GENERATORTABLE",pkColumnName="ENTITYNAME",pkColumnValue="MyEntity",valueColumnName="PKVALUE",allocationSize=1)
	 * @GeneratedValue(strategy = GenerationType.TABLE,generator="myGenerator")
	 * 以下是测试产生的SQL：
	 * 
		Hibernate: 
		    drop table if exists generatortable1
		Hibernate: 
		    drop table if exists t_address
		Hibernate: 
		    create table generatortable1 (
		        pkColumn1 varchar(255) not null,
		        value bigint,
		        primary key (pkColumn1)
		    )
		Hibernate: 
		    create table t_address (
		        id bigint not null,
		        city varchar(255),
		        country varchar(255),
		        street varchar(255),
		        primary key (id)
		    )
		    
		第一次insert产生的SQL：
		Hibernate: 
		    select
		        tbl.value 
		    from
		        generatortable1 tbl 
		    where
		        tbl.pkColumn1=? for update
		            
		Hibernate: 
		    insert 
		    into
		        generatortable1
		        (pkColumn1, value)  
		    values
		        (?,?)
		Hibernate: 
		    update
		        generatortable1 
		    set
		        value=?  
		    where
		        value=? 
		        and pkColumn1=?
		Hibernate: 
		    insert 
		    into
		        t_address
		        (city, country, street, id) 
		    values
		        (?, ?, ?, ?)	
		        
		第二次insert产生的SQL:
		Hibernate: 
		    select
		        tbl.value 
		    from
		        generatortable1 tbl 
		    where
		        tbl.pkColumn1=? for update
		            
		Hibernate: 
		    update
		        generatortable1 
		    set
		        value=?  
		    where
		        value=? 
		        and pkColumn1=?
		Hibernate: 
		    insert 
		    into
		        t_address
		        (city, country, street, id) 
		    values
		        (?, ?, ?, ?)		
    
	 */
	
	/*
	@Id
	@TableGenerator(name="myGenerator",table="generatortable1",pkColumnName="pkColumn1",pkColumnValue="t_address",valueColumnName="value",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE,generator="myGenerator")
	private Long id;
	*/
	
	/*
	@Id
	@Column(name="CODE")
	private Long code;
	*/
}
