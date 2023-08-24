package br.dev.marcelodeoliveira.rest.model;

public class UserAuth {
	private String email, senha;

	private UserAuth() {
	};

	public UserAuth(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

	public String getSenha() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	public String getEmail() {
		// TODO Auto-generated method stub
		return this.email;
	}

}
