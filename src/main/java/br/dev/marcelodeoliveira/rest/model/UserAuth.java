package br.dev.marcelodeoliveira.rest.model;

public class UserAuth {
	private String email, senha;

	public UserAuth() {
	};

	public UserAuth(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

	public String getSenha() {		
		return this.senha;
	}

	public String getEmail() {		
		return this.email;
	}

}
