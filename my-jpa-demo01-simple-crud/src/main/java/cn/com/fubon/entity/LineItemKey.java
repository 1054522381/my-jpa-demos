package cn.com.fubon.entity;
import java.io.Serializable;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class LineItemKey implements Serializable {
	private Integer orderId;
	private int itemId;

	/**
	 * 必须要有hashCode()方法
	 */
	public int hashCode() {
		return ((this.getOrderId() == null ? 0 : this.getOrderId().hashCode()) ^ ((int) this
				.getItemId()));
	}

	/**
	 * 必须要有equals方法
	 */
	public boolean equals(Object otherOb) {
		if (this == otherOb) {
			return true;
		}
		if (!(otherOb instanceof LineItemKey)) {
			return false;
		}
		LineItemKey other = (LineItemKey) otherOb;
		return ((this.getOrderId() == null ? other.orderId == null : this
				.getOrderId().equals(other.orderId)) && (this.getItemId() == other.itemId));
	}
}
