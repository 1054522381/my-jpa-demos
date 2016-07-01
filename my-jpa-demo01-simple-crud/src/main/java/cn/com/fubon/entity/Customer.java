package cn.com.fubon.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity{
	private String name;
	
	@Column(unique=true)
	private EmailAddress emailAddress;
	
	/*
	 * 单向关联
	 */
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name="customer_id")
	private Set<Address> addresses = new HashSet<Address>();
	
	@Override
	public String toString() {
		return "name=" + name + ";emailAddress=" + emailAddress.getEmailAddress()
				+ ";addresses=" + Arrays.toString(addresses.toArray());
	}
}
