package rest.model.requests;

public class ContaRequest {
	public String nome;

	public ContaRequest() {
	}

	public ContaRequest(String nome) {
		setNome(nome);
	}

	public String setNome(String nome) {
		// TODO Auto-generated method stub
		return (this.nome = nome);
	}

	public String getNome() {
		// TODO Auto-generated method stub
		return this.nome;
	}

}
