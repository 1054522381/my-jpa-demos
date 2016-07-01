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

	/*保存Person可以级联持久化关联的IdCard，会先保存IdCard，再保存Person，不会产生update语句*/
	@Test
	public void testInsertPerson() {
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

	/*IdCard作为外键的被维护方*/
	@Test
	public void testInsertIdCard() {
		IdCard card = new IdCard();
		card.setNumber("111111");
		card.setExpireDate(new Date());
		
		Person person = new Person();
		person.setName("张三");
		person.setCard(card); //外键由外键维护方设置
		card.setPerson(person);
		
		manager.getTransaction().begin();
		manager.persist(card);
		manager.getTransaction().commit();
	}
	/*查询IdCard的号码和人名*/
	@Test
	public void testFindIdCardById() {
		IdCard card = manager.find(IdCard.class, 1L);
		System.out.println(String.format("name==>%s,cardNumber==>%s", card.getPerson().getName(),card.getNumber()));
	}
	
	/*API删除托管状态下的实体Person*/
	@Test
	public void testRemovePerson() {
		Person person = manager.find(Person.class, 1L);
		manager.getTransaction().begin();
		manager.remove(person);
		manager.getTransaction().commit();
	}
	

	/*API删除托管状态下的实体IdCard*/
	@Test
	public void testRemoveIdCard() {
		IdCard card = manager.find(IdCard.class, 1L);
		manager.getTransaction().begin();
		//删除card之前需要解除外键关系
		card.getPerson().setCard(null);
		manager.remove(card);
		manager.getTransaction().commit();
	}

}
