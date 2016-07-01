package cn.com.fubon.entity;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DepartmentTest {
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

	/*
	 * 会产生一个update语句。
	 */
	@Test
	public void testInsert() {
		Employee employee = new Employee();
		employee.setEname("张三");
		employee.setSalary(BigDecimal.valueOf(100));
		
		Department dept = new Department();
		dept.setDname("信息部");
		
		Set<Employee> emps = new HashSet<>();
		emps.add(employee);
		dept.setEmployees(emps);
		
		manager.getTransaction().begin();
		manager.persist(dept);
		manager.getTransaction().commit();
	}
	
	/*查询Department的名称和雇员名称*/
	@Test
	public void testFindById() {
		Department dept = manager.find(Department.class, 1L);
		System.out.println(String.format("dname==>%s", dept.getDname()));
		for(Employee emp : dept.getEmployees()){
			System.out.println(String.format("ename==>%s",emp.getEname()));
		}
	}
	
	/*创建并更新部门*/
	@Test
	public void testUpdate() {
		Department dept = manager.find(Department.class, 1L);
		dept.setDname("设计部");
		for(Employee emp : dept.getEmployees()){
			emp.setEname("王五");
			break;
		}
		manager.getTransaction().begin();
		manager.persist(dept);
		manager.getTransaction().commit();
	}
	
	/*API级联删除托管状态下的实体Department，映射表和雇员*/
	@Test
	public void testRemove() {
		Department dept = manager.find(Department.class, 1L);
		manager.getTransaction().begin();
		manager.remove(dept);
		manager.getTransaction().commit();
	}
	
	/*sql删除Department，会删除映射表，但是不会级联删除雇员*/
	@Test
	public void testDelete() {
		manager.getTransaction().begin();
		manager.createQuery("delete Department e where e.id = ?1")
		.setParameter(1, 1L).executeUpdate();
		manager.getTransaction().commit();
	}
	
}
