package rest.model;

import java.time.LocalDate;

import rest.model.enums.TipoTransacao;

public class Transacao {
	
	String contaId,
	usuarioId, //não necessário
	descicao,
	encolvido;

	TipoTransacao tipo;// (DESP / REC) --> enum
	LocalDate data_transacao, data_pagamento;// (dd/MM/YYYY) ---> String (Format f) -> // (dd/MM/YYYY)
	Float valor;// (0.00f)
	boolean status;// (true / false) //transação concluída ou pendente

}
