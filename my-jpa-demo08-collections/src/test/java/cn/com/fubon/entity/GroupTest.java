package cn.com.fubon.entity;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupTest {

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
	public void testInsertUser() {
		Group group = new Group();
		group.setName("五科");
		User user1 = new User();
		user1.setName("张三");
		user1.setGroup(group);
		manager.getTransaction().begin();
		manager.persist(user1);
		manager.getTransaction().commit();
	}
	
	@Test
	public void testInsertGroup() {
		Group group = new Group();
		group.setName("五科");
		User user1 = new User();
		user1.setName("张三");
		user1.setGroup(group);
		group.getUsers().put(3L, user1); //指定了@MapKey，3L并没有起作用
		manager.getTransaction().begin();
		manager.persist(group);
		System.out.println("users==>" + group.getUsers());
		manager.getTransaction().commit();
	}

	@Test
	public void testLoadGroup() {
		Group group = (Group)manager.getReference(Group.class, 1L);
		for(Map.Entry<Long, User> entry : group.getUsers().entrySet()) {
			System.out.println("user name=>" + entry.getValue().getName());
		}
		
	}
	
	@Test
	public void testFindGroup() {
		/*虽然persist指定了写入的mapkey是3L，但其实不起作用，还是以ID为key*/
		Group group = (Group) manager.find(Group.class, 1L);
		System.out.println("users==>" + group.getUsers());

	}
}
