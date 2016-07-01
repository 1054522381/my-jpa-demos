package cn.com.fubon.entity;

/* 默认入库的是枚举类型的索引，从0开始 */
public enum EmployeeType {

	FULL_TIME_EMPLOYEE("full_time"), //0
	PART_TIME_EMPLOYEE("part_time"),  //1
	CONTRACT_EMPLOYEE("contract");  //2
	
	private String name;
	
	private EmployeeType(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "_" + this.name;
	}
}
