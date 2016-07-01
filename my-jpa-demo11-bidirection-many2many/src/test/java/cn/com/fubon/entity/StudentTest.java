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

public class StudentTest {
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

	/*保存Student级联Teacher*/
	@Test
	public void testInsertStudent() {
		Student stu = new Student();
		stu.setSname("张三");
		
		Teacher teacher = new Teacher();
		teacher.setTname("张三老师");
		Set<Teacher> teachers = new HashSet<>();
		teachers.add(teacher);
		stu.setTeachers(teachers);
		
		manager.getTransaction().begin();
		manager.persist(stu);
		manager.getTransaction().commit();
	}

	@Test
	public void testInsertTeacher() {
		Teacher teacher = new Teacher();
		teacher.setTname("李四老师");
		
		Student stu = new Student();
		stu.setSname("李四");
		Set<Teacher> teachers = new HashSet<>();
		teachers.add(teacher);
		stu.setTeachers(teachers); //注意要由关系维护方写值，如果stu没有指定teachers，则中间表不会产生记录
		Set<Student> stus = new HashSet<>();
		stus.add(stu);
		
		teacher.setStudents(stus);
		manager.getTransaction().begin();
		manager.persist(teacher);
		manager.getTransaction().commit();
	}
	
	/*查询Student的名称和老师名称*/
	@Test
	public void testFindByStudentId() {
		Student stu = manager.find(Student.class, 1L);
		System.out.println(String.format("sname==>%s,tname==>%s", 
				stu.getSname(),stu.getTeachers().iterator().next().getTname()));
	}

	/*查询Teacher的名称和学生名称*/
	@Test
	public void testFindByTeacherId() {
		Teacher teacher = manager.find(Teacher.class, 1L);
		System.out.println(String.format("tname==>%s", teacher.getTname()));
		for(Student stu : teacher.getStudents()){
			System.out.println(String.format("sname==>%s", stu.getSname()));
		}
	}
	
	/*API删除托管状态下的实体Teacher和中间表，注意学生表不应该删除记录*/
	@Test
	public void testRemove() {
		Teacher t = manager.find(Teacher.class, 1L);
		manager.getTransaction().begin();
		for(Student student : t.getStudents()){
			student.getTeachers().remove(t); //移除关联关系
		}
		manager.remove(t);
		manager.getTransaction().commit();
	}
	
}
