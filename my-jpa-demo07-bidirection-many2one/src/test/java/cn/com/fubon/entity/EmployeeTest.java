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

	/*保存Employee级联Department，不会产生update语句*/
	@Test
	public void testInsertEmployee() {
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
	

	/*
	 * 保存Department级联Employee
	 * 如果Department中没有设置Employee级联，persist只会保存Department，不会报错
	 * */
	@Test
	public void testInsertDepartment() {
		Department dept = new Department();
		dept.setDname("商品部");
		
		Employee employee = new Employee();
		employee.setEname("李四");
		employee.setSalary(BigDecimal.valueOf(100));
		employee.setDepartment(dept); //注意：Employee维护外键，外键不设置不会有值
		Set<Employee> emps = new HashSet<>();
		emps.add(employee);
		
		dept.setEmps(emps);
		manager.getTransaction().begin();
		manager.persist(dept);
		manager.getTransaction().commit();
	}
	
	/*查询Employee的名称和部门名称*/
	@Test
	public void testFindByEmployeeId() {
		Employee employee = manager.find(Employee.class, 1L);
		System.out.println(String.format("ename==>%s,dname==>%s", employee.getEname(),employee.getDepartment().getDname()));
	}

	/*查询Department的名称和雇员名称*/
	@Test
	public void testFindByDepartmentId() {
		Department dept = manager.find(Department.class, 1L);
		System.out.println(String.format("dname==>%s", dept.getDname()));
		for(Employee emp : dept.getEmps()){
			System.out.println(String.format("ename==>%s", emp.getEname()));
		}
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
	
	/*API级联删除托管状态下的实体Department和雇员*/
	@Test
	public void testRemove() {
		Department dept = manager.find(Department.class, 1L);
		manager.getTransaction().begin();
		manager.remove(dept);
		manager.getTransaction().commit();
	}
	
}
