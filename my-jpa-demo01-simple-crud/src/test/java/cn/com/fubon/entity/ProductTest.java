package cn.com.fubon.entity;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {
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
	public void testInsert() throws UnsupportedEncodingException {
		Product product = new Product();
		product.setName("p1");
		product.setDescription("description1".getBytes("UTF-8"));
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("key1", "value1");
		attributes.put("key2", "value2");
		product.setAttributes(attributes);
		manager.getTransaction().begin();
		manager.persist(product);
		manager.getTransaction().commit();
		
		Product productDb = manager.find(Product.class, 1L);
		System.out.println(String.format("description==>%s", new String(productDb.getDescription(),"UTF-8")));
	
	}
	
}
