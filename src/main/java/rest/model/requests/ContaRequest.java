package rest.model.requests;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContaRequest {
	public String nome;
	
	 public ContaRequest() {}

	 public ContaRequest(String nome) {
		 this.nome = nome;
	}

}

