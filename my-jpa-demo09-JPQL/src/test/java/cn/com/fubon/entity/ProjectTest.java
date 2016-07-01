package cn.com.fubon.entity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
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
		Project designProject = new DesignProject();
		designProject.setName("dp1");
		manager.getTransaction().begin();
		manager.persist(designProject);
		manager.getTransaction().commit();
	}
	
	/*
	 * type类型表达式的结果是实体的名称。
	 */
	@Test
	public void testFind() {
		String ql = "select p from Project p where type(p) = DesignProject";
		List<Project> projects = manager.createQuery(ql,Project.class)
				.getResultList();
		for(Project project : projects){
			System.out.println("type1==>" + project.getClass());
		}
		
		/*
		 * 如下用传参的方式查询，要传class对象
		 */
		ql = "select p from Project p where type(p) = ?1";
		projects = manager.createQuery(ql,Project.class)
				.setParameter(1,DesignProject.class)
				.getResultList();
		for(Project project : projects){
			System.out.println("type2==>" + project.getClass());
		}
	}
	
}
