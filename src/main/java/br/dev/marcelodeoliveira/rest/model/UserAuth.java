package br.dev.marcelodeoliveira.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserAuth {
	public String email, senha;
	public UserAuth() {};
	
	public UserAuth(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

}
