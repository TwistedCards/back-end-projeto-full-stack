package com.fullStack.backEnd.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transferencia")
public class Transferencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "valor_transferido")
	private Double valorTransferido;

	@Column(name = "conta_origem")
	@NotEmpty(message = "{campo.contaOrigem.obrigatorio}")
	private String contaOrigem;
	
	@Column(name = "valor_destino")
	@NotEmpty(message = "{campo.contaDestino.obrigatorio}")
	private String contaDestino;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(name = "data_transferencia")
	@JsonFormat(pattern = "dd/MM/yyyy")
	@NotEmpty(message = "{campo.dataTransferencia.obrigatorio}")
	private String dataTransferencia;

	@Column(name = "data_agendamento")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataAgendamento;

	public Transferencia() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValorTransferido() {
		return valorTransferido;
	}

	public void setValorTransferido(double valorTransferido) {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
