package cn.com.fubon.entity;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jodd.datetime.JDateTime;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SQLQuery;
import org.hibernate.jpa.QueryHints;
import org.hibernate.transform.Transformers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * 路径表达式：是查询的构建基块。用于从一个实体中导航，通过关系到达另一个实体（或者实体集合）或实体的一个持久化属性。
 * 分为：状态字段路径；单值关联路径；集合值关联路径。
 * 点操作符（.）表示一个表达式中的导航路径。
 * 路径表达式不限为单个导航。路径不能从一个状态字段或集合值关联继续。路径表达式可以导航到和跨过嵌入对象以及正常的实体。
 * 路径表达式对嵌入对象的唯一限制是路径表达式的根必须从一个实体开始。
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
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
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
	
	/*一级缓存，只会有一次SQL
	 * 注：如果没有查询到记录的话，会产生两次SQL
	 * */
	@Test
	public void testManagerCache(){
		Phone phone1 = manager.find(Phone.class, 1L);
		Phone phone2 = manager.find(Phone.class, 1L);
	}
	
	/*二级缓存，需要在实体上标识@Cacheable(true)，只会有一次SQL
	 * */
	@Test
	public void testManagerFactoryCache(){
		Phone phone1 = manager.find(Phone.class, 1L);
		
		manager = factory.createEntityManager();
		Phone phone2 = manager.find(Phone.class, 1L);
	}
	
	/*
	 * 一下查询正常是打两条SQL，启用查询缓存后只输出一条SQL
	 */
	@Test
	public void testQueryCache(){
		Query query = manager.createQuery("select p from Phone p");
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		List result1 = query.getResultList();
		System.out.println("----------------");
		query = manager.createQuery("select p from Phone p");
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		result1 = query.getResultList();
		
	}
	
	/*
	 * 相同的位置参数可以在查询中出现多次，绑定到该参数的值将替代它的每次出现。
	 * 推荐使用命名查询，避免了连续解析JPQL和生成SQL的系统开销。
	 */
	@Test
	public void testPositionParam() {
		String ql = "select e from Employee e where e.departt.id = ?1 and e.id = ?1";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.setParameter(1, 1L).getResultList();
		for(Employee emp : emps){
			System.out.println(String.format("emp.name==>%s", emp.getName()));
		}
	}
	
	/*
	 * 可以查集合,但是如果用TypedQuery指定返回类型为Employee.class会出错。
	 */
	@Test
	public void testQueryCollections() {
		String ql = "select d.emps from Department d";
		List<Employee> emps = manager.createQuery(ql)
				.getResultList();
		for(Employee emp : emps){
			System.out.println(String.format("ename==>%s", emp.getName()));
		}
	}
	
	/*
	 * != 会转成 <> 比较操作符,jpql支持类似SQL的比较操作符、between、like、and、or、is [not] null等
	 */
	@Test
	public void testComparisonOperators() {
		String ql = "select e.name,e.salary from Employee e where e.salary <> 99";
		List<Object[]> result1 = manager.createQuery(ql).getResultList();
		for(Object[] arr : result1){
			System.out.println(String.format("emp.name==>%s,emp.salary==>%s",arr[0],arr[1]));
		}
		
		ql = "select e.name,e.salary from Employee e where e.salary != 99";
		List<Object[]> result2 = manager.createQuery(ql).getResultList();
		for(Object[] arr : result2){
			System.out.println(String.format("emp.name==>%s,emp.salary==>%s",arr[0],arr[1]));
		}
	}
	
	/*
	 * escape转义下划线或者%，指定一个字符，当它作为通配符的前缀时，指示通配符应该被当做文字。
	 */
	@Test
	public void testLikeEscape(){
		String ql = "select e.name from Employee e where e.address.state like 'QA#_%' escape '#'";
		List<String> result1 = manager.createQuery(ql).getResultList();
		for(String name : result1){
			System.out.println(String.format("emp.name==>%s",name));
		}
	}
	
	/*
	 * 子查询
	 */
	@Test
	public void testSubQuery(){
		/*
		 * 查询工资为部门最高工资的员工
		 */
		String ql = "select e from Employee e where e.salary = (select max(emp.salary) from Employee emp)";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.getResultList();
		System.out.println("工资为部门最高工资的员工:");
		for(Employee emp : emps){
			System.out.println(String.format("ename==>%s,salary==>%s", emp.getName(),emp.getSalary()));
		}
		
		/*
		 * 查询使用XIAOMI手机的员工
		 */
		ql = "select e from Employee e where exists (select 1 from e.phones p where p.type='XIAOMI')";
		List<Employee> result = manager.createQuery(ql,Employee.class)
				.getResultList();
		System.out.println("使用XIAOMI手机的员工:");
		for(Employee emp : result){
			System.out.println(String.format("ename==>%s,dname==>%s", emp.getName(),emp.getDepartt().getName()));
		}
	}
	
	/*
	 * 用in查询员工数量大于1的部门的员工
	 */
	@Test
	public void testIn(){
		/* size(collection)函数返回集合中的元素数量，如果集合为空，将返回0 */
		String ql = "select e from Employee e where e.departt in (select distinct d from Department d where size(d.emps)>1)";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.getResultList();
		for(Employee emp : emps){
			System.out.println(String.format("ename==>%s,dname==>%s", emp.getName(),emp.getDepartt().getName()));
		}
	}
	
	@Test
	public void testCaseWhen(){
		StringBuilder ql = new StringBuilder();
		ql.append("select e.name,");
		ql.append("       case e.type");
		ql.append("         when 'FULL_TIME_EMPLOYEE' then");
		ql.append("          'full_time'");
		ql.append("         when 'PART_TIME_EMPLOYEE' then");
		ql.append("          'part_time'");
		ql.append("         when 'CONTRACT_EMPLOYEE' then");
		ql.append("          'contract'");
		ql.append("         else");
		ql.append("          'unknown type'");
		ql.append("       end as type");
		ql.append("  from Employee e");
		List<Object[]> result = manager.createQuery(ql.toString())
				.getResultList();
		for(Object[] arr : result){
			System.out.println(String.format("ename==>%s,etype==>%s", arr[0],arr[1]));
		}
	}
	
	/*
	 * CASE表达式的合并表达式形式，接受多个表达式，第一个返回非空值的表达式将成为表达式的结果。
	 */
	@Test
	public void testCoalesce(){
		String ql = "select e.name,coalesce(e.address.street,e.address.city,e.address.state) from Employee e)";
		List<Object[]> result = manager.createQuery(ql)
				.getResultList();
		for(Object[] arr : result){
			System.out.println(String.format("ename==>%s,address==>%s",arr[0],arr[1]));
		}
	}
	
	/*
	 * CASE表达式的NULLIF表达式形式，接受两个表达式，如果两个表达式的结果相等，返回NULL，否则返回第一个表达式的结果。
	 */
	@Test
	public void testNullIf(){
		String ql = "select nullif(d.name,'商品部') from Department d";
		List result = manager.createQuery(ql).getResultList();
		for(Object o : result){
			System.out.println(String.format("result==>%s",o));
		}
	}
	
	@Test
	public void testFindDistinctColumns() {
		String ql = "select distinct e.name from Employee e";
		List<String> names = manager.createQuery(ql).getResultList();
		for(String name : names){
			System.out.println(String.format("emp.name==>%s", name));
		}
	}

	@Test
	public void testFindByWhere() {
		String ql = "select e from Employee e,Department d where d = e.departt and e.name=:ename and d.name=:dname";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.setParameter("ename", "zhangsan")
				.setParameter("dname", "信息部")
				.getResultList();
		for(Employee emp : emps){
			for(Phone phone : emp.getPhones()){
				System.out.println(String.format("number==>%s",phone.getNumber() ));
			}
		}
		
	}

	/*
	 * JOIN操作符可以使用集合值关联的路径表达式和单值关联的路径表达式。
	 */
	@Test
	public void testFindByJoin1() {
		String ql = "select p.number from Employee e join e.phones p where e.departt.name=:dname and p.type=:ptype";
		List<String> numbers = manager.createQuery(ql)
		 .setParameter("dname", "信息部")
		 .setParameter("ptype", "XIAOMI")
		 .getResultList();
		for(String number : numbers){
			System.out.println("number==>" + number);
		}
	}

	/*
	 * 查询处理器从左到右解释FROM子句。一旦已声明一个变量，税后就可以由其它join表达式引用。
	 */
	@Test
	public void testFindByJoin2() {
		String ql = "select p.number from Department d join d.emps e join e.phones p where d.name='信息部'";
		List<String> numbers = manager.createQuery(ql)
		 .getResultList();
		for(String number : numbers){
			System.out.println("number==>" + number);
		}
	}
	@Test
	public void testFindByNamedQuery() {
		List<Employee> emps = manager.createNamedQuery("Employee.findByName", Employee.class)
		.setParameter("name", "zhangsan")
		.getResultList();
		
		for(Employee emp : emps){
			for(Phone phone : emp.getPhones()){
				System.out.println(String.format("number==>%s",phone.getNumber() ));
			}
		}
	}


	@Test
	public void testFindByNamedQuery2() {
		Department dept = new Department();
		dept.setId(1L);
		dept.setName("信息部");
		List<Employee> emps = manager.createNamedQuery("Employee.findByDept", Employee.class)
		.setParameter("dept", dept)
		.getResultList();
		
		for(Employee emp : emps){
			System.out.println(String.format("emp.name==>%s",emp.getName() ));
		}
	}
	
	@Test
	public void testFindByDate(){
		JDateTime jDateTime = new JDateTime();
		String ql = "select e from Employee e where e.birthDay between ?1 and ?2";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.setParameter(1, jDateTime.convertToDate())
				.setParameter(2, jDateTime.convertToDate())
				.getResultList();
		for(Employee emp : emps){
			System.out.println(String.format("emp.name==>%s",emp.getName() ));
		}
	}
	
	/*
	 * 集合表达式：is [not] empty
	 */
	@Test
	public void testFindByEmptyEntity(){
		String ql = "select e from Employee e where e.phones is empty";
		List<Employee> emps = manager.createQuery(ql,Employee.class)
				.getResultList();
		for(Employee emp : emps){
			System.out.println(String.format("emp.name==>%s",emp.getName() ));
		}
	}

	@Test
	public void testFindColumns() {
		String ql = "select e.name,e.departt.name from Employee e order by e.id desc";
		List<Object[]> emps = manager.createQuery(ql).getResultList();
		for(Object[] emp : emps){
			System.out.println(String.format("emp.name==>%s,emp.departt.name==>%s", emp[0],emp[1]));
		}
	}

	@Test
	public void testNativeQueryMap(){
	    Query query = manager.createNativeQuery("select id, name, salary from employee");  
	    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);  
	    List rows = query.getResultList();  
	    for (Object obj : rows) {  
	        Map row = (Map) obj;  
	        System.out.println(
	        	String.format("id=%s,name=%s,salary=%s",row.get("id"),row.get("name"),row.get("salary"))) ; 
	    }  
	}  
	
	/*
	 * testNativeQuery1 测试时如果是select id 会报错，无法转成long类型。
	 */
	@Test
	public void testNativeQueryDto(){
	    Query query = manager.createNativeQuery("select name, birthDay from employee");  
	    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(EmployeeDto.class));  
	    List rows = query.getResultList();  
	    for (Object obj : rows) {  
	    	EmployeeDto row = (EmployeeDto) obj;  
	        System.out.println(
	        	String.format("name=%s,birthDay=%s",row.getName(),row.getBirthDay())) ; 
	    }  
	}

	/*
	private Long id;
	IllegalArgumentException occurred while calling setter for property [cn.com.fubon.entity.Employee.id (expected type = java.lang.Long)]; target = [cn.com.fubon.entity.Employee@1393abe4], property value = [1]
	 */
	@Test
	public void testNativeQueryId(){
	    Query query = manager.createNativeQuery("select id, name from employee");
	    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Employee.class));
	    List rows = query.getResultList();
	    for (Object obj : rows) {
	    	Employee row = (Employee) obj;
	        System.out.println(
	        	String.format("id=%s,name=%s",row.getId(),row.getName())) ;
	    }
	}
	
	@Test
	public void testFindDtosByNEW(){
		String ql = "select new cn.com.fubon.entity.EmployeeDto(e.name,e.birthDay) from Employee e where e.phones is empty";
		List<EmployeeDto> empDtos = manager.createQuery(ql,EmployeeDto.class)
				.getResultList();
		for(EmployeeDto empDto : empDtos){
			System.out.println(String.format("empDto.name==>%s,empDto.birthDay==>%s",empDto.getName(),empDto.getBirthDay().toLocaleString() ));
		}
	}
	
	@Test
	public void testRemoveEntityByQuery(){
		String ql = "delete from Employee e where e.name=:name";
		manager.getTransaction().begin();
		manager.createQuery(ql).setParameter("name", "lisi").executeUpdate();
		manager.getTransaction().commit();
	}
	

	@Test
	public void testFindByPage() {
		String ql = "select e.name from Employee e";
		List<String> names = manager.createQuery(ql)
				.setFirstResult(1)
				.setMaxResults(1)
				.getResultList();
		for(String name : names){
			System.out.println(String.format("emp.name==>%s", name));
		}
	}
	
	@Test
	public void testOrderBy() {
		String ql = "select e.name from Employee e order by e.salary desc";
		List<String> names = manager.createQuery(ql)
				.getResultList();
		for(String name : names){
			System.out.println(String.format("emp.name==>%s", name));
		}
	}
	
	@Test
	public void testFetch(){
		String ql = "select d from Department d";
		List<Department> depts = manager.createQuery(ql,Department.class)
				.getResultList();
		for(Department dept : depts){
			System.out.println("============= lazy 查询已完成 =================");
//			for(Employee emp : dept.getEmps()){
//				System.out.println(String.format("phonesType==>%s", emp.getPhones().getClass()));
//				break;
//			}
			break;
		}
		
		/*
		 * fetch指示关系应该在查询执行时即时解析
		 */
		ql = "select d from Department d join fetch d.emps ";
		depts = manager.createQuery(ql,Department.class)
				.getResultList();
		for(Department dept : depts){
			System.out.println("============= fetch 查询已完成 =================");
//			for(Employee emp : dept.getEmps()){
//				System.out.println(String.format("fetch phonesType==>%s", emp.getPhones().getClass()));
//				break;
//			}
			break;
		}
	}
	
	@Test
	public void testAggregateQuery() {
		/*
		 * 所有员工的平均工资
		 */
		String ql = "select avg(e.salary) from Employee e";
		Object avgSalary = manager.createQuery(ql)
				.getSingleResult();
		System.out.println("所有员工的平均工资:");
		System.out.println(String.format("type==>%s,avgSalary==>%s",avgSalary.getClass(),avgSalary));
		
		/*
		 * 每个部门的平均工资
		 */
		ql = "select d.name,count(e),avg(e.salary) from Department d join d.emps e group by d.name";
		List<Object[]> result1 = manager.createQuery(ql).getResultList();
		System.out.println("每个部门的平均工资:");
		for(Object[] arr : result1){
			System.out.println(String.format("dname==>%s,empNum==>%s,avgSalary==>%s",arr[0],arr[1],arr[2]));
		}
		
		/*
		 * 每个部门的平均工资且平均工资大于15000
		 */
		ql = "select d.name,avg(e.salary) from Department d join d.emps e group by d.name having avg(e.salary)>15000";
		List<Object[]> result2 = manager.createQuery(ql).getResultList();
		System.out.println("每个部门的平均工资且平均工资大于15000:");
		for(Object[] arr : result2){
			System.out.println(String.format("dname==>%s,avgSalary==>%s",arr[0],arr[1]));
		}
		
		/*
		 * 每个部门有多少员工,count返回Long类型值
		 */
		ql = "select d,count(e) from Department d join d.emps e group by d having count(e)>0";
		List<Object[]> d = manager.createQuery(ql)
				.getResultList();
		System.out.println("每个部门有多少员工:");
		for(Object[] dd : d){
			System.out.println(String.format("dname==>%s,countType==>%s,empNum==>%s",((Department)dd[0]).getName(),dd[1].getClass(), dd[1]));
		}
	}
	
}
