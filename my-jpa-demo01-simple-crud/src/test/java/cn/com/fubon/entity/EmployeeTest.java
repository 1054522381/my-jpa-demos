package cn.com.fubon.entity;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 单表增删改查
 */
public class EmployeeTest {
	private EntityManager manager;
	private EntityManagerFactory factory;

	@Before
	public void setup(){
		Map<String,Object> properties = new HashMap<>();
		properties.put("hibernate.show_sql", true);
		factory = Persistence.createEntityManagerFactory("unit1", properties);
		//factory = Persistence.createEntityManagerFactory("unit1");
		manager = factory.createEntityManager();
	}
	
	@After
	public void teardown(){
		if(manager != null){
			manager.close();
		}
			
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
		Employee employee = new Employee();
		employee.setType(EmployeeType.FULL_TIME_EMPLOYEE);
		manager.getTransaction().begin();
		manager.persist(employee);
		manager.getTransaction().commit();
		System.out.println(String.format("id==>%s,type==>%s",employee.getId(),employee.getType()));
	}
	
	/*
	 * persist一个已经是持久化状态的对象，会什么也不做，不会产生insert或者update。
	 */
	@Test
	public void testPersist1() {
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		manager.persist(employee);
		manager.getTransaction().commit();
	}
	
	/*
	 * persist一个游离状态的对象，有id，但没有和持久化上下文关联。
	 */
	@Test
	public void testPersist2() {
		Employee employee = new Employee();
		employee.setId(2L);
		employee.setType(EmployeeType.PART_TIME_EMPLOYEE);
		manager.getTransaction().begin();
		manager.persist(employee); //会抛异常javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: detached entity passed to persist: cn.com.fubon.entity.Employee
		manager.getTransaction().commit();
	}

	/*
	 * persist一个删除状态的对象。先remove，不提交，再persist，转成持久化状态。
	 */
	@Test
	public void testPersist3() {
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		manager.remove(employee);
		manager.persist(employee);
		System.out.println("--------type-------" + employee.getType());
		employee.setType(EmployeeType.PART_TIME_EMPLOYEE);
		manager.getTransaction().commit();
	}

	@Test
	public void testUpdate() {
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		employee.setType(EmployeeType.CONTRACT_EMPLOYEE);
		manager.getTransaction().commit();
		System.out.println(String.format("type==>%s", employee.getType()));
	}
	
	@Test
	public void testFindById() {
		Employee employee = manager.find(Employee.class, 1L);
		System.out.println("--------------------");
		System.out.println(String.format("class==>%s,type==>%s",employee.getClass().getName(), employee.getType()));
	}
	
	/**
	 * 查不到记录会抛异常，返回的是代理对象
	 */
	@Test
	public void testGetReferenceById() {
		Employee employee = manager.getReference(Employee.class, 1L);
//		manager.close(); //org.hibernate.LazyInitializationException: could not initialize proxy - no Session
		System.out.println("--------------------");
		System.out.println(String.format("class==>%s,type==>%s",employee.getClass().getName(), employee.getType()));
	}
	
	@Test
	public void testRemove() {
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		manager.remove(employee);
		manager.getTransaction().commit();
	}
	
	/**
	 * new对象merge入库会返回一个带有id的新对象
	 */
	@Test
	public void testMergeNew() {
		Employee employee1 = new Employee();
		employee1.setType(EmployeeType.PART_TIME_EMPLOYEE);
		manager.getTransaction().begin();
		Employee employee2 = manager.merge(employee1);
		manager.getTransaction().commit();
		System.out.println(
				String.format("employee1.id==>%s,employee2.id==>%s",employee1.getId(),employee2.getId()));
	}

	/**
	 * 游离（脱管）对象merge：
	 * manager缓存中不存在且数据库中不存在时，会根据该游离对象复制一个新的对象做insert；
	 * 比如，该测试中的employee1.id=100L并不会被持久化。
	 */
	@Test
	public void testMergeDetachedNotExistsInDBAndCache() {
		Employee employee1 = new Employee();
		employee1.setType(EmployeeType.PART_TIME_EMPLOYEE);
		employee1.setId(100L);
		manager.getTransaction().begin();
		Employee employee2 = manager.merge(employee1);
		manager.getTransaction().commit();
		System.out.println(
				String.format("employee1.id==>%s,employee2.id==>%s",employee1.getId(),employee2.getId()));
	}

	/**
	 * 游离（脱管）对象merge：
	 * manager缓存中不存在但数据库中存在时，会查询该游离对象复制该游离对象的属性做update；
	 */
	@Test
	public void testMergeDetachedNotExistsInCache() {
		Employee employee1 = new Employee();
		employee1.setType(EmployeeType.CONTRACT_EMPLOYEE);
		employee1.setId(1L);
		manager.getTransaction().begin();
		Employee employee2 = manager.merge(employee1);
		manager.getTransaction().commit();
		System.out.println("==>" + (employee1 == employee2));
	}

	/**
	 * 游离（脱管）对象merge：
	 * manager缓存中存在对应的游离对象，会复制该游离对象至缓存对象，对缓存对象做update；
	 */
	@Test
	public void testMergeDetachedExistsInCache() {
		Employee employee1 = new Employee();
		employee1.setType(EmployeeType.PART_TIME_EMPLOYEE);
		employee1.setId(1L);
		Employee employee2 = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		manager.merge(employee1);
		manager.getTransaction().commit();
		System.out.println("==>" + (employee1 == employee2));
	}
	
	@Test
	public void testFlush(){
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		employee.setType(EmployeeType.FULL_TIME_EMPLOYEE);
		manager.flush(); //flush在事务提交之前会发出 update SQL
		System.out.println("----------------");
		manager.getTransaction().commit(); //正常是在事务提交的时候发出 update SQL
	}
	
	/**
	 * 由于存在一级缓存只会发一条查询SQL，refresh的话会多发一条。
	 */
	@Test
	public void testRefresh(){
		Employee employee = manager.find(Employee.class, 1L);
		employee = manager.find(Employee.class, 1L);
		System.out.println("-------------------");
		manager.refresh(employee);
	}
	
}
