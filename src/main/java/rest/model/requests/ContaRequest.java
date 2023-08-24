package rest.model.requests;

public class ContaRequest {
	public String nome;

	public ContaRequest() {
	}

	public ContaRequest(String nome) {
		setNome(nome);
	}

	public String setNome(String nome) {
		
		return (this.nome = nome);
	}

	public String getNome() {
		
		return this.nome;
	}

}
