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
 * Embedded
 */
public class CompanyTest {
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
		Company company = new Company();
		
		Address address = new Address();
		address.setCity("厦门");
		address.setStreet("东渡路");
		company.setAddress(address);
		
		manager.getTransaction().begin();
		manager.persist(company);
		manager.getTransaction().commit();
		System.out.println(String.format("company.id==>%s", company.getId()));
	}
	
	@Test
	public void testFindById() {
		Employee employee = manager.find(Employee.class, 1L);
		System.out.println(String.format("depart.name==>%s", employee.getDepartt().getName()));
	}
	
}
