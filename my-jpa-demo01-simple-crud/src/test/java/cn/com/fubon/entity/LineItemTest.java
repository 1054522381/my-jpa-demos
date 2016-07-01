package cn.com.fubon.entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试联合主键
 */
public class LineItemTest {
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
		LineItem lineItem = new LineItem();
		lineItem.setItemId(222);
		lineItem.setOrderId(3333);
		lineItem.setName("lineItem1");
		manager.getTransaction().begin();
		manager.persist(lineItem);
		manager.getTransaction().commit();
	}
	
	
}
