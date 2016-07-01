package cn.com.fubon.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product extends AbstractEntity{
	@Column(length=2555)
	private String name;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	private byte[] description;
	
	private BigDecimal price;
	
	@ElementCollection
	private Map<String,String> attributes = new HashMap<String,String>();
}
