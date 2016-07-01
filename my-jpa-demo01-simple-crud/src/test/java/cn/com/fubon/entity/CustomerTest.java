package cn.com.fubon.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest {
	
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
		Customer customer = new Customer();
		customer.setName("zhangsan");
		EmailAddress emailAddress = new EmailAddress("12312@qq.com");
//		EmailAddress emailAddress = new EmailAddress("12312sdfsd");
		customer.setEmailAddress(emailAddress);
		Address address = new Address();
		address.setCountry("CN");
		address.setCity("xiamen");
		customer.getAddresses().add(address);
		manager.getTransaction().begin();
		manager.persist(customer);
		manager.getTransaction().commit();
		System.out.println(String.format("id==>%s", customer.getId()));
	}
	

	@Test
	public void testFind() {
		String sql = "select * from customer where name = :name";
		Customer customer = (Customer)manager.createNativeQuery(sql, Customer.class)
				.setParameter("name", "zhangsan")
				.getSingleResult();
		System.out.println(customer);
	}
}
