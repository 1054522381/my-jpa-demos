package cn.com.fubon.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue(value="Design")
@Getter
@Setter
public class DesignProject extends Project{
}
