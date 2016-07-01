package cn.com.fubon.entity;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 单表增删改查
 */
public class AddressTest {
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


	@Test
	public void testInsert() {
		Address address = new Address();
		address.setCity("xm");
		address.setCountry("CN");
		/*
		 * 更改数据需要开启事务 
		 */
		manager.getTransaction().begin();
		manager.persist(address);
		manager.getTransaction().commit();
		System.out.println(String.format("id==>%s", address.getId()));
	}
	
	@Test
	public void testFindById() {
		Address address = manager.find(Address.class, 2L);
		System.out.println(String.format("city==>%s", address.getCity()));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testFindByJPQL() {
		/* jpql from + entity的名字 */
		List<Address> list = manager.createQuery("select a from addr a where a.country = ?0")
		.setParameter(0, "CN")
		.getResultList();
		for(Address address : list){
			System.out.println("address=>" + address);
		}
		
		//查询具体的属性
		List citys = manager.createQuery("select a.city from addr a where a.country = ?0")
		.setParameter(0, "CN")
		.getResultList();
		for(Object city : citys){
			System.out.println("city=>" + city);
		}
	}
	
	@Test
	public void testCountByJPQL() {
		/* jpql from + entity的名字 */
		Object count = manager.createQuery("select count(a) from addr a where a.city = ?0")
		.setParameter(0, "xm")
		.getSingleResult();
		System.out.println("count=>" + count);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPageByJPQL() {
		
/* 
INSERT INTO `t_address` (`city`, `country`, `street`, `customer_id`) VALUES ('sy', 'cn', NULL, NULL);
INSERT INTO `t_address` (`city`, `country`, `street`, `customer_id`) VALUES ('fz', 'cn', NULL, NULL);
INSERT INTO `t_address` (`city`, `country`, `street`, `customer_id`) VALUES ('sm', 'cn', NULL, NULL);
INSERT INTO `t_address` (`city`, `country`, `street`, `customer_id`) VALUES ('bj', 'cn', NULL, NULL);
*/
		List<Address> list = manager.createQuery("select a from addr a")
		.setFirstResult(0)
		.setMaxResults(3)
		.getResultList();
		for(Address address : list){
			System.out.println("address=>" + address);
		}
	}

	@Test
	public void testUpdate() {
		Address address = manager.find(Address.class, 2L);
		if(address != null){
			System.out.println(String.format("old city==>%s", address.getCity()));
			manager.getTransaction().begin();
			address.setCity("厦门");
			/* 托管状态的实体更新不需要显示persist,manager.persist(address)，但是也需要开启事务;*/
			manager.getTransaction().commit();
			System.out.println(String.format("new city==>%s", address.getCity()));
		}
	}
	

	@Test
	public void testDelete() {
		Address address = manager.find(Address.class, 1L);
		if(address != null){
			manager.getTransaction().begin();
			//只能删除持久化上下文中的托管实体,否则报错,IllegalArgumentException:Removing a detached instance...
			manager.remove(address);
			manager.getTransaction().commit();
			System.out.println(String.format("removed id==>%s", address.getId()));
		}
		
	}

	@Test
	@Ignore
	public void testDelete2() {
		Address address = new Address();
		address.setId(4L);
		address.setCity("bj");
		address.setCountry("cn");
		manager.getTransaction().begin();
		//只能删除持久化上下文中的托管实体,测试失败
		manager.remove(address);
		manager.getTransaction().commit();
		System.out.println(String.format("removed id==>%s", address.getId()));
	}
}
