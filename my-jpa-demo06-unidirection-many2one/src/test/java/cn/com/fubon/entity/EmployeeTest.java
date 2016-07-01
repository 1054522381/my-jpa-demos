package cn.com.fubon.entity;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmployeeTest {
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

	/*保存Employee可以级联持久化关联的Department，不会产生update语句*/
	@Test
	public void testInsert() {
		Employee employee = new Employee();
		employee.setEname("张三");
		employee.setSalary(BigDecimal.valueOf(100));
		
		Department dept = new Department();
		dept.setDname("信息部");
		employee.setDepartment(dept);
		
		manager.getTransaction().begin();
		manager.persist(employee);
		manager.getTransaction().commit();
	}
	
	/*查询Employee的名称和部门名称*/
	@Test
	public void testFindById() {
		Employee employee = manager.find(Employee.class, 1L);
		System.out.println(String.format("ename==>%s,dname==>%s", employee.getEname(),employee.getDepartment().getDname()));
	}
	
	/*创建并更新部门*/
	@Test
	public void testUpdate() {
		Employee employee = manager.find(Employee.class, 1L);
		employee.setEname("王五");
		Department dept = new Department();
		dept.setDname("设计部");
		employee.setDepartment(dept);
		manager.getTransaction().begin();
		manager.persist(employee);
		manager.getTransaction().commit();
	}
	
	/*API删除托管状态下的实体Employee*/
	@Test
	public void testRemove() {
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		manager.remove(employee);
		manager.getTransaction().commit();
	}
	
	/*sql删除Employee*/
	@Test
	public void testDelete() {
		manager.getTransaction().begin();
		manager.createQuery("delete Employee e where e.id = ?1")
		.setParameter(1, 1L).executeUpdate();
		manager.getTransaction().commit();
	}
	
	/*
	* 游离(脱管)状态
	* Detached entity instances have a persistent identify and are not currently associated with a persistence context.
	* 注:entityManager.clear()可以将实体管理器中的所有实体变成游离状态,对游离状态实体的set操作不会被持久化,用entityManager.merge(xxx)可以.
	*/
	@Test
	public void testDetached() {
		Employee employee = manager.find(Employee.class, 1L);
		manager.getTransaction().begin();
		manager.clear();
		System.out.println("contains==>" + manager.contains(employee));
		Department dept = new Department();
		dept.setDname("商品部");
		employee.setEname("李四");
		employee.setDepartment(dept);
		manager.merge(employee); //需要设置Department的merge级联
		manager.getTransaction().commit();
	}

}
