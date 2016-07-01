package cn.com.fubon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(LineItemKey.class)
@Getter
@Setter
public class LineItem {
	@Id
	private Integer orderId;
	@Id
	@Column(name="itemCode")
	private int itemId;
	private String name;
}
