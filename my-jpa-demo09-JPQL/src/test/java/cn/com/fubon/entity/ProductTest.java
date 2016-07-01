package cn.com.fubon.entity;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jodd.datetime.JDateTime;
import org.apache.commons.beanutils.BeanUtils;
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
	public void testInsert() {
		CarProduct carProduct = new CarProduct();
		carProduct.setName("carProduct1");
		carProduct.setField2("field2");
		manager.getTransaction().begin();
		manager.persist(carProduct);
		System.out.println(String.format("id==>%s", carProduct.getId()));
		manager.getTransaction().commit();
	}
	
}
