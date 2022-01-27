package com.fullStack.backEnd.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fullStack.backEnd.model.Transferencia;

public class TransferenciaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Double valorTransferido;
	private String contaOrigem;
	private String contaDestino;
	private String nome;
	private String dataTransferencia;
	private LocalDate dataAgendamento;

	public TransferenciaDTO() {
	}
	
	public TransferenciaDTO(Transferencia tranferencia) {
		super();
		this.id = tranferencia.getId();
		this.valorTransferido = tranferencia.getValorTransferido();
		this.contaOrigem = tranferencia.getContaOrigem();
		this.contaDestino = tranferencia.getContaDestino();
		this.nome = tranferencia.getUsuario().getNome();
		this.dataTransferencia = tranferencia.getDataTransferencia();
		this.dataAgendamento = tranferencia.getDataAgendamento();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValorTransferido() {
		return valorTransferido;
	}

	public void setValorTransferido(Double valorTransferido) {
		this.valorTransferido = valorTransferido;
	}

	public String getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(String contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public String getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(String contaDestino) {
		this.contaDestino = contaDestino;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataTransferencia() {
		return dataTransferencia;
	}

	public void setDataTransferencia(String dataTransferencia) {
		this.dataTransferencia = dataTransferencia;
	}

	public LocalDate getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(LocalDate dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

}
