package rest.model.requests;

public class TransacaoRequestSimpleType {


	private Integer conta_id;
	private String descricao;
	private String envolvido;
	private String tipoTransacao;
	private String data_transacao;
	private String data_pagamento;
	private Float valor;
	private Boolean status;
	private Integer transferencia_id = null;
	private Integer parcelamento_id = null;
	private String observacao = null;




	public TransacaoRequestSimpleType(Integer conta_id, String descricao, 
			String envolvido, String tipoTransacao, String data_transacao,
			String data_pagamento, Float valor, Boolean status) {
		super();
		this.conta_id = conta_id;
		this.descricao = descricao;
		this.envolvido = envolvido;
		this.tipoTransacao = tipoTransacao;
		this.data_transacao = data_transacao;
		this.data_pagamento = data_pagamento;
		this.valor = valor;
		this.status = status;
	}

	public TransacaoRequestSimpleType(
			Integer conta_id,
			String descricao,
			String envolvido,
			String tipo,
			String data_transacao,
			String data_pagamento,
			Float valor,
			Boolean status,
			Integer transferencia_id,
			Integer parcelamento_id,
			String observacao) {
		super();
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

	public String getTipotransacao() {
		return tipoTransacao;
	}

	public String getData_transacao() {
		return data_transacao;
	}

	public String getData_pagamento() {
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

	public void setTipo(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public void setData_transacao(String data_transacao) {
		this.data_transacao = data_transacao;
	}

	public void setData_pagamento(String data_pagamento) {
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
