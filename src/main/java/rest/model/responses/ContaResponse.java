package rest.model.responses;

public class ContaResponse {
	public Integer id;
	public	Integer usuario_id;
	public String nome;
	public String visivel;

	public ContaResponse() {
	}

	public ContaResponse(Integer id,  String nome, String visivel, Integer usuario_id) {
		setId(id);
		setNome(nome);
		setUsuarioId(usuario_id);
		setVisible(visivel);
	}

	private Integer setId(Integer id) {
		return this.id = id;
	}

	private String setVisible(String visivel) {
		return this.visivel = visivel;
	}

	private Integer setUsuarioId(Integer usuario_id) {
		return this.usuario_id = usuario_id;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUsuarioId() {
		return usuario_id;
	}

	public String getNome() {
		return this.nome;
	}

	public String isVisible() {
		return this.visivel;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
