package br.dev.marcelodeoliveira.rest.model.enums;

public enum TipoTransacao {
	
	REC("receber"),
	DESP("despesa");
	
	private String transacaoString;
	private TipoTransacao(String transacaoString) {
		this.transacaoString = transacaoString;
	}
	
	public String getTransacaoString() {
		return this.transacaoString;
	}
}
