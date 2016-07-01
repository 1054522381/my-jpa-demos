package cn.com.fubon.entity;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;

@Embeddable
@Getter
@Setter
public class EmailAddress {
	private static final String EMAIL_REGEX = "^(.+)@(.+)$";
	private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);
	
	@Column(name="email")
	private String emailAddress;
	
	protected EmailAddress(){
		
	}
	
	public EmailAddress(String emailAddress){
		Assert.assertTrue("invalid email address!",isValid(emailAddress));
		this.emailAddress = emailAddress;
	}
	
	public boolean isValid(String candidate){
		return PATTERN.matcher(candidate).matches();
	}
}
