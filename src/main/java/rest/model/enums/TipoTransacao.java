package rest.model.enums;

public enum TipoTransacao {
	
	REC("receber"),
	DES("despesa");
	
	private String transacaoString;
	private TipoTransacao(String transacaoString) {
		this.transacaoString = transacaoString;
	}
	
	public String getTransacaoString() {
		return this.transacaoString;
	}
}
