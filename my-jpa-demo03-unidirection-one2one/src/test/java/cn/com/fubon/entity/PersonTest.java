package cn.com.fubon.entity;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	private EntityManager manager;
	private EntityManagerFactory factory;

	@Before
	public void setup(){
		factory = Persistence.createEntityManagerFactory("unit1");
		manager = factory.createEntityManager();
	}
	
	@After
	public void teardown(){
		manager.close();
		factory.close();
	}
	
	/**
	 * 查看自动生成的表
	 */
	@Test
	public void createTable() {
		
	}

	/*保存Person可以级联持久化关联的IdCard，会先保存IdCard，再保存Person，没有产生update语句*/
	@Test
	public void testInsert() {
		Person person = new Person();
		person.setName("张三");
		
		IdCard card = new IdCard();
		card.setNumber("111111");
		card.setExpireDate(new Date());
		person.setCard(card);
		
		manager.getTransaction().begin();
		manager.persist(person);
		manager.getTransaction().commit();
	}
	
	/*查询Person的名称和IdCard号码*/
	@Test
	public void testFindById() {
		Person person = manager.find(Person.class, 1L);
		System.out.println(String.format("name==>%s,cardNumber==>%s", person.getName(),person.getCard().getNumber()));
	}
	
	/*
	* 游离(脱管)状态
	* Detached entity instances have a persistent identify and are not currently associated with a persistence context.
	* 注:entityManager.clear()可以将实体管理器中的所有实体变成游离状态,对游离状态实体的set操作不会被持久化,用entityManager.merge(xxx)可以.
	*/
	@Test
	public void testDetached() {
		Person person = manager.find(Person.class, 1L);
		System.out.println("before clear contains==>" + manager.contains(person));
		manager.getTransaction().begin();
		manager.clear();
		System.out.println("after clear contains==>" + manager.contains(person));
		person.getCard().setNumber("111112");
		manager.merge(person); //需要设置IdCard的merge级联
		manager.getTransaction().commit();
	}
	
	/*API删除托管状态下的实体Person*/
	@Test
	public void testRemove() {
		Person person = manager.find(Person.class, 1L);
		manager.getTransaction().begin();
		manager.remove(person);
		manager.getTransaction().commit();
	}

}
