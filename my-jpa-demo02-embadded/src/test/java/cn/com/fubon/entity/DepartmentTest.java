package cn.com.fubon.entity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 一对多
 */
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


	/**
	 * 单向(unidirectional)一对多，
	 * 如果加了mapBy指定双向关联，该测试会无法写入外键列
	 */
	@Test
	public void testInsert() {
		Department department = new Department();
		department.setName("信息部");
		Employee employee1 = new Employee();
		employee1.setName("张三");
		employee1.setType(EmployeeType.FULL_TIME_EMPLOYEE);
		Set<Employee> emps = new HashSet<Employee>();
		emps.add(employee1);
		department.setEmps(emps);
		manager.getTransaction().begin();
		manager.persist(department);
		manager.getTransaction().commit();
		System.out.println(String.format("employee1.id==>%s", department.getEmps().iterator().next().getId()));
	}
	
	@Test
	public void testFindById() {
		Department department = manager.find(Department.class, 1L);
		System.out.println(String.format("department.employee1.name==>%s", department.getEmps().iterator().next().getName()));
	}
	
}
