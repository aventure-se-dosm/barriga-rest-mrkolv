package rest.model.enums;
enum tipoTrans{
	REC("receita"),
	DESP("despesa");
	String tipoString;
	private tipoTrans(String transacao) {}
}