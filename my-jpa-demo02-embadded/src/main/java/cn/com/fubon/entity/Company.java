package cn.com.fubon.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Company extends AbstractEntity{
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="state",column=@Column(name="province")),
				@AttributeOverride(name="zip",column=@Column(name="postal_code")		)
	})
	private Address address;
}
