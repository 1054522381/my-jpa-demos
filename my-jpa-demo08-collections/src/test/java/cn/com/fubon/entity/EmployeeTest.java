package cn.com.fubon.entity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 多对一
 */
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


	/**
	 * 多对一
	 */
	@Test
	public void testInsert() {
		Employee employee = new Employee();
		employee.setName("zhangsan");
		
		Vacation vacation1 = new Vacation();
		vacation1.setDaysTaken(10);
		vacation1.setStartDate(Calendar.getInstance());
		
		Vacation vacation2 = new Vacation();
		vacation2.setDaysTaken(7);
		vacation2.setStartDate(Calendar.getInstance());
		
		Collection<Vacation> vacations = new ArrayList<>();
		vacations.add(vacation1);
		vacations.add(vacation2);
		employee.setVacations(vacations);
		
		Set<String> nickNames = new HashSet<>();
		nickNames.add("zhangsan_n1");
		nickNames.add("zhangsan_n2");
		employee.setNickNames(nickNames);
		
		manager.getTransaction().begin();
		manager.persist(employee);
		manager.getTransaction().commit();
		System.out.println(String.format("id==>%s", employee.getId()));
	}
	
	@Test
	public void testFindById() {
		Employee employee = manager.find(Employee.class, 1L);
	}
	
}
