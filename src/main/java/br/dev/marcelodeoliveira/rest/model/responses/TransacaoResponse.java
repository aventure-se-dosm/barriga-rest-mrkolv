package br.dev.marcelodeoliveira.rest.model.responses;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

enum tipoTrans{
	REC("receita"),
	DESP("despesa");
	String tipoString;
	private tipoTrans(String transacao) {}
}

@Getter @Setter
public class TransacaoResponse {


	private Integer id;
	private String descricao;
	private String envolvido;
	private String observacao = null;
	private String tipo;
	private Date data_transacao;
	private Date data_pagamento;
	private Float valor;
	private Boolean status;
	private Float conta_id;
	private Float usuario_id;
	private String transferencia_id = null;
	private String parcelamento_id = null;
	

	// Getter Methods

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getEnvolvido() {
		return envolvido;
	}

	public String getObservacao() {
		return observacao;
	}

	public String getTipo() {
		return tipo;
	}

	public Date getData_transacao() {
		return data_transacao;
	}

	public Date getData_pagamento() {
		return data_pagamento;
	}

	public Float getValor() {
		return valor;
	}

	public boolean getStatus() {
		return status;
	}

	public Float getConta_id() {
		return conta_id;
	}

	public Float getUsuario_id() {
		return usuario_id;
	}

	public String getTransferencia_id() {
		return transferencia_id;
	}

	public String getParcelamento_id() {
		return parcelamento_id;
	}

	// Setter Methods

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setEnvolvido(String envolvido) {
		this.envolvido = envolvido;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setData_transacao(Date data_transacao) {
		this.data_transacao = data_transacao;
	}

	public void setData_pagamento(Date data_pagamento) {
		this.data_pagamento = data_pagamento;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setConta_id(Float conta_id) {
		this.conta_id = conta_id;
	}

	public void setUsuario_id(Float usuario_id) {
		this.usuario_id = usuario_id;
	}

	public void setTransferencia_id(String transferencia_id) {
		this.transferencia_id = transferencia_id;
	}

	public void setParcelamento_id(String parcelamento_id) {
		this.parcelamento_id = parcelamento_id;
	}
}
