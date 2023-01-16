package br.com.fernando.tests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movimentacao {
	
	private Integer id;
	private String descricao;
	private String envolvido;
	private String tipo;
	private String data_transacao;
	private String data_pagamento;
	private Float valor;
	private Boolean status;
	private Integer conta_id;
	private Integer usuario_id;

}
