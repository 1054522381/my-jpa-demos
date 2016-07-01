package cn.com.fubon.entity;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/*
 * CriteriaBuilder接口是通向条件API的主要网关，其为各种对象充当一个工厂，这些对象会链接在一起形成一个查询定义。
 */
public class EmployeeTest {
	private EntityManager manager;
	private EntityManagerFactory factory;
	private CriteriaBuilder cb;

	@Before
	public void setup(){
		factory = Persistence.createEntityManagerFactory("unit1");
		manager = factory.createEntityManager();
		cb = manager.getCriteriaBuilder();
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
	public void testInsert() throws IllegalAccessException, InvocationTargetException {
		Employee employee1 = new Employee();
		employee1.setType(EmployeeType.FULL_TIME_EMPLOYEE);
		employee1.setName("zhangsan");
		employee1.setBirthDay(new Date());
		employee1.setSalary(BigDecimal.valueOf(10000));
		Department department = new Department();
		department.setName("信息部");
		employee1.setDepartt(department);
		
		Set<Project> projects = new HashSet<>();
		Project project1 = new DesignProject();
		project1.setName("project1");
		projects.add(project1);
		employee1.setProjects(projects);
		
		Phone phone1 = new Phone();
		phone1.setType("XIAOMI");
		phone1.setNumber("15880254575");
		Set<Phone> phones = new HashSet<>();
		phones.add(phone1);
		employee1.setPhones(phones);
		
		Address address = new Address();
		address.setCity("厦门");
		address.setStreet("东渡路");
		address.setState("QA_EAST");
		employee1.setAddress(address);
		
		Employee employee2 = new Employee();
		BeanUtils.copyProperties(employee2,employee1);
		employee2.setType(EmployeeType.PART_TIME_EMPLOYEE);
		Department department2 = new Department();
		department2.setName("商品部");
		employee2.setName("lisi");
		employee2.setBirthDay(new Date());
		employee2.setSalary(BigDecimal.valueOf(20000));
		employee2.setDepartt(department2);
		Address address2 = new Address();
		BeanUtils.copyProperties(address2,address);
		address2.setStreet(null);
		address2.setCity("福州");
		address2.setState("TT_EAST");
		employee2.setAddress(address2);
		employee2.setPhones(null);
		//manager.getTransaction().begin();


		manager.getTransaction().begin();
		manager.persist(employee1);
		//manager.getTransaction().commit();
		System.out.println(String.format("departt_id==>%s", employee1.getDepartt().getId()));
	
		
		manager.persist(employee2);
		manager.getTransaction().commit();
		System.out.println(String.format("departt_id==>%s", employee2.getDepartt().getId()));
	}

	/*
	 * 通过from()返回Root对象，建立查询的根，就相当于在JPQL中声明标识变量。
	 * select()的参数必须是一种与查询定义的结果类型兼容的类型。
	 * get()方法返回一个Path对象，但是该Path对象一直是Path<Object>类型。
	 */
	@Test
	public void testCriteriaQuery() {
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> from = cq.from(Employee.class);
		cq.select(from)
				.where(cb.equal(from.get("salary"),10000));
		List<Employee> emps = manager.createQuery(cq).getResultList();
		System.out.println("=======查询薪资等于10000的员工========");
		for(Employee e : emps){
			System.out.println(String.format("ename=>%s,salary=>%s,departt class=>%s\n"
					,e.getName(),e.getSalary(),e.getDepartt().getClass()));
		}
	}

	@Test
	public void testCriteriaQueryFetchOrderBy() {
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> from = cq.from(Employee.class);
		from.fetch("departt"); //及时加载department
		cq.orderBy(cb.desc(from.get("salary")));
		List<Employee> emps = manager.createQuery(cq).getResultList();
		System.out.println("=====查询所有员工，根据薪资降序排列========");
		for(Employee e : emps){
			System.out.print(String.format("ename=>%s,salary=>%s,departt class=>%s\n"
					,e.getName(),e.getSalary(),e.getDepartt().getClass()));
		}

	}

	/*
	 * SQL会带distinct关键字
	 * from.<Number>get("salary")的写法有些特别，<String>限定了类型
	 * */
	@Test
	public void testCriteriaQueryDistinct() {
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class)
				.distinct(true);
		Root<Employee> from = cq.from(Employee.class);
		Predicate condition = cb.gt(from.<Number>get("salary"), 10000);
		cq.where(condition);
		TypedQuery<Employee> typedQuery = manager.createQuery(cq);
		List<Employee> emps = typedQuery.getResultList();
		
		for(Employee e : emps){
			System.out.println(String.format("ename=>%s,salary=>%s",e.getName(),e.getSalary()));
		}
	}
	
	/*
	类似于JPQL：
		select distinct d
		from department d,employee e
		where d = e.department
	SQL：
	select
        distinct department1_.id as id1_0_,
        department1_.name as name2_0_ 
    from
        Employee employee0_ cross 
    join
        Department department1_ 
    where
        department1_.id=employee0_.depart_id
	 */
	@Test
	public void testCriteriaQueryFroms() {
		CriteriaQuery<Department> cq = cb.createQuery(Department.class);
		Root<Employee> fromEmp = cq.from(Employee.class);
		Root<Department> fromDept = cq.from(Department.class);
		cq.select(fromDept).distinct(true)
			.where(cb.equal(fromDept, fromEmp.get("departt")));
		List<Department> depts = manager.createQuery(cq).getResultList();
		
		for(Department d : depts){
			System.out.println(String.format("dname=>%s",d.getName()));
		}
	}
	
	/*
	类似于：
		select e
		from employee e
		where e.address.city = '厦门'
	 */
	@Test
	public void testCriteriaQueryPaths() {
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> fromEmp = cq.from(Employee.class);
		cq.select(fromEmp)
			.where(cb.equal(fromEmp.get("address").get("city"), "厦门"));
		List<Employee> emps = manager.createQuery(cq).getResultList();
		
		for(Employee e : emps){
			System.out.println(String.format("ename=>%s",e.getName()));
		}
	}
	
	/*
	 * 返回单值
	 */
	@Test
	public void testCriteriaQuerySingleValue() {
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Employee> fromEmp = cq.from(Employee.class);
		cq.select(fromEmp.<String>get("name"));
		List<String> names = manager.createQuery(cq).getResultList();
		
		for(String name : names){
			System.out.println(String.format("ename=>%s",name));
		}
	}
	
	/*
	 * construct查Dto，需要有匹配的构造方法。
    select
        employee0_.name as col_0_0_,
        employee0_.birthDay as col_1_0_ 
    from
        Employee employee0_
	 */
	@Test
	public void testCriteriaQueryConstruct() {
		CriteriaQuery<EmployeeDto> cq = cb.createQuery(EmployeeDto.class);
		Root<Employee> fromEmp = cq.from(Employee.class);
		cq.select(cb.construct(EmployeeDto.class, fromEmp.get("name"),fromEmp.get("birthDay")));
		List<EmployeeDto> emps = manager.createQuery(cq).getResultList();
		
		for(EmployeeDto e : emps){
			System.out.println(String.format("ename=>%s",e.getName()));
		}
	}

	/*
	 * 
	select e,count(p)
	from employee e join e.phones p
	group by e
	having count(p)>=2
	 */
	@Test
	public void testCriteriaQueryGroupBy() {
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Employee> fromEmp = cq.from(Employee.class);
		Join<Employee, Phone> phones = fromEmp.join("phones");
		
		cq.multiselect(fromEmp,cb.count(phones)).groupBy(fromEmp).having(cb.ge(cb.count(phones), 2));
		List<Object[]> emps = manager.createQuery(cq).getResultList();
		
		for(Object[] e : emps){
			System.out.println(String.format("ename=>%s",e));
		}
	}
	
	@Test
	public void testCriteriaQueryTuple(){
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Employee> emp = cq.from(Employee.class);
		cq.select(cb.tuple(emp.get("id"),emp.get("name")));
		List<Tuple> emps = manager.createQuery(cq).getResultList();
		for(Tuple e : emps){
			System.out.print(String.format("id=>%s,name=>%s\n",e.get(0),e.get(1)));
		}
	}
	
	@Test
	public void testCriteriaQueryTupleAlias(){
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Employee> emp = cq.from(Employee.class);
		cq.select(cb.tuple(emp.get("id").alias("id"),emp.get("name").alias("ename")));
		List<Tuple> emps = manager.createQuery(cq).getResultList();
		for(Tuple e : emps){
			System.out.print(String.format("id=>%s,name=>%s\n",e.get("id"),e.get("ename")));
		}
	}

	/*
	 * 参数表达式
	 */
	@Test
	public void testCriteriaQueryParameterExpression(){
		ParameterExpression<String> paramExp = cb.parameter(String.class, "name");
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> fromEmp = cq.from(Employee.class);
		Predicate predicate = cb.conjunction();
		predicate = cb.and(predicate,cb.equal(fromEmp.get("name"),paramExp));
		
		cq.select(fromEmp)
			.where(predicate);
		List<Employee> emps = manager.createQuery(cq)
				.setParameter("name", "zhangsan").getResultList();
		for(Employee e : emps){
			System.out.print(String.format("id=>%s,name=>%s\n",e.getId(),e.getName()));
		}
	}
	
}
