package in.krish.binding;

import lombok.Data;

@Data
public class LoginForm {
	
	private String email ;
	private String password ;
	private String clientId;

	public String ip;
}
