package rest.model.requests;

import java.time.LocalDateTime;

import rest.model.enums.TipoTransacao;

public class TransacaoRequest {
	private Integer conta_id;
	private String descricao;
	private String envolvido;
	private TipoTransacao tipoTransacao;
	private LocalDateTime data_transacao;
	private LocalDateTime data_pagamento;
	private Float valor;
	private Boolean status;
	private Integer transferencia_id = null;
	private Integer parcelamento_id = null;
	private String observacao = null;

	public TransacaoRequest(Integer conta_id, String descricao, String envolvido, TipoTransacao tipo,
			LocalDateTime data_transacao, LocalDateTime data_pagamento, Float valor, Boolean status,
			Integer transferencia_id, Integer parcelamento_id, String observacao) {

		this.conta_id = conta_id;
		this.descricao = descricao;
		this.envolvido = envolvido;
		this.tipoTransacao = tipo;
		this.data_transacao = data_transacao;
		this.data_pagamento = data_pagamento;
		this.valor = valor;
		this.status = status;
		this.transferencia_id = transferencia_id;
		this.parcelamento_id = parcelamento_id;
		this.observacao = observacao;
	}

	public TransacaoRequest(Integer conta_id, String descricao, String envolvido, TipoTransacao tipoTransacao,
			LocalDateTime data_transacao, LocalDateTime data_pagamento, Float valor, Boolean status) {

		this(conta_id, descricao, envolvido, tipoTransacao, data_transacao, data_pagamento, valor, status, null, null,
				null);

	}

	// Getter Methods

	public String getDescricao() {
		return descricao;
	}

	public String getEnvolvido() {
		return envolvido;
	}

	public String getObservacao() {
		return observacao;
	}

	public TipoTransacao getTipotransacao() {
		return tipoTransacao;
	}

	public LocalDateTime getData_transacao() {
		return data_transacao;
	}

	public LocalDateTime getData_pagamento() {
		return data_pagamento;
	}

	public Float getValor() {
		return valor;
	}

	public boolean getStatus() {
		return status;
	}

	public Integer getConta_id() {
		return conta_id;
	}

	public Integer getconta_id() {
		return conta_id;
	}

	public Integer getTransferencia_id() {
		return transferencia_id;
	}

	public Integer getParcelamento_id() {
		return parcelamento_id;
	}

	// Setter Methods

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setEnvolvido(String envolvido) {
		this.envolvido = envolvido;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setTipo(TipoTransacao tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public void setData_transacao(LocalDateTime data_transacao) {
		this.data_transacao = data_transacao;
	}

	public void setData_pagamento(LocalDateTime data_pagamento) {
		this.data_pagamento = data_pagamento;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setConta_id(Integer conta_id) {
		this.conta_id = conta_id;
	}

	public void setconta_id(Integer conta_id) {
		this.conta_id = conta_id;
	}

	public void setTransferencia_id(Integer transferencia_id) {
		this.transferencia_id = transferencia_id;
	}

	public void setParcelamento_id(Integer parcelamento_id) {
		this.parcelamento_id = parcelamento_id;
	}
}
