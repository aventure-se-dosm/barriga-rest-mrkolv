package rest.model.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
		setvisivel(visivel);
	}

	public Integer setId(Integer id) {
		return this.id = id;
	}

	public String setvisivel(String visivel) {
		return this.visivel = visivel;
	}

	public Integer setUsuarioId(Integer usuario_id) {
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

	public String isvisivel() {
		return this.visivel;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
