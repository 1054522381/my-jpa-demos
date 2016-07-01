package cn.com.fubon.entity;
import java.util.HashSet;
import java.util.List;
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
		employee.setType(EmployeeType.FULL_TIME_EMPLOYEE);
		employee.setName("zhangsan");
		Department department = new Department();
		department.setName("信息部");
		employee.setDepartt(department);
		
		Phone phone1 = new Phone();
		phone1.setType("XIAOMI");
		phone1.setNumber("15880254575");
		Set<Phone> phones = new HashSet<>();
		phones.add(phone1);
		employee.setPhones(phones);
		
		Address address = new Address();
		address.setCity("厦门");
		address.setStreet("东渡路");
		employee.setAddress(address);
		
		manager.getTransaction().begin();
		manager.persist(employee);
		manager.getTransaction().commit();
		System.out.println(String.format("departt_id==>%s", employee.getDepartt().getId()));
	}
	
	/*
	 * 对选择查询返回的嵌入对象作更改，返回的对象不是托管的，并不会持久化到db。
	 */
	@Test
	public void testQueryEmbaddedObj1() {
		manager.getTransaction().begin();
		String ql = "select e.address from Employee e";
		List<Address> addrs = manager.createQuery(ql,Address.class)
				.getResultList();
		for(Address addr : addrs){
			System.out.println(String.format("city==>%s", addr.getCity()));
			addr.setCity("北京");
		}
		
		manager.getTransaction().commit();
	}
	
	/*
	 * 通过托管实体导航到嵌入对象，再更改嵌入对象，会持久化到db。
	 */
	@Test
	public void testQueryEmbaddedObj2() {
		manager.getTransaction().begin();
		String ql = "select e from Employee e";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.getResultList();
		for(Employee emp : emps){
			System.out.println(String.format("emp.address.city==>%s", emp.getAddress().getCity()));
			emp.getAddress().setCity("北京");
		}
		
		manager.getTransaction().commit();
	}
}
