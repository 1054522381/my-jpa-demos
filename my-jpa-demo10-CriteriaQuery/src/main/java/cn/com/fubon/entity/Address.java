package cn.com.fubon.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@Setter
public class Address {
	private String street;
	private String city;
	private String state;
	@Column(name="zip_code")
	private String zip;
}
