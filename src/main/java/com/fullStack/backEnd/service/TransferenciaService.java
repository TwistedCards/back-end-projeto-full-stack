package com.fullStack.backEnd.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullStack.backEnd.model.Conta;
import com.fullStack.backEnd.model.Transferencia;
import com.fullStack.backEnd.model.Usuario;
import com.fullStack.backEnd.repository.TransferenciaRepository;

@Service
public class TransferenciaService {

	@Autowired
	private TransferenciaRepository repository;
	
	@Autowired
	private UsuarioService userService;
	
	@Autowired
	private ContaService contaService;
	
	private Usuario user = new Usuario();
	
	public double calculandoTransferenciaA(double valorTransferido){
		double resultado = valorTransferido + (3 * 5.44);
		return resultado;
	}
	
	public double calculandoTransferenciaB(double valorTransferido) {
		return valorTransferido + (12 * 5.44);
	}
	
	public double calculandoTransferenciaC(double valorTransferido, long dias) {
		if(dias > 10 && dias <= 20) {
			// 8.2%
			return valorTransferido * 1.082;
		} else if(dias > 20 && dias <= 30) {
			// 6.9%
			return valorTransferido * 1.069;	
		} else if(dias > 30 && dias <= 40) {
			// 4.7%
			return valorTransferido * 1.047;
		} else if(dias > 40) {
			// 1.7%
			return valorTransferido * 1.017;
		} else {
			return valorTransferido;
		}
	}

	@Transactional
	public Transferencia insert(Transferencia obj) {
		obj.setId(null);
		obj = repository.save(obj);
		return obj;
	}
	
	public Transferencia processandoTransferencia(Transferencia transferencia) {
		user = new Usuario();
		List<Usuario> listObjUsuario = userService.findAll();
		
		transferencia.setDataAgendamento(LocalDate.now());
		LocalDate dateTransferencia = LocalDate.parse(transferencia.getDataTransferencia());
		
		if(null == contaService.findByConta(transferencia.getContaOrigem()) || 
				null == contaService.findByConta(transferencia.getContaDestino())) {
//			return new ResponseEntity<Transferencia>(transferencia, HttpStatus.BAD_REQUEST);
			return transferencia;
		}
		
		Conta contaOrigem = contaService.findByConta(transferencia.getContaOrigem());
		Conta contaDestino = contaService.findByConta(transferencia.getContaDestino());
		
		transferencia.setNomeDestino(userService.findByConta(contaDestino).getNome());
		
		listObjUsuario.stream()
						.filter(usuario -> usuario.getConta().getId() 
												== contaOrigem.getId())
						.forEach(usuario -> {
							user = usuario;
						});
		
		transferencia.setUsuario(user);
		
		long diferencaDeDias = transferencia.getDataAgendamento().until(dateTransferencia, ChronoUnit.DAYS);
		double valorTransferido = transferencia.getValorTransferido();
		
		extractedRegrasDeTransferencia(transferencia, dateTransferencia, diferencaDeDias, valorTransferido);
		
		if(user.getConta().getValor() >= transferencia.getValorTransferido()) {
			if(dateTransferencia.isEqual(transferencia.getDataAgendamento())){
				extractedSubtracaoValorDaConta(transferencia);
				userService.saveR(user);
			}
			
			transferencia = this.insert(transferencia);
		}
		
//		return new ResponseEntity<Transferencia>(transferencia, HttpStatus.OK);
		return transferencia;
	}
	
	private void extractedRegrasDeTransferencia(Transferencia transferencia, LocalDate dateTransferencia,
			long diferencaDeDias, double valorTransferido) {
		if(dateTransferencia.isEqual(transferencia.getDataAgendamento()) ||
				(valorTransferido > 0 && valorTransferido <= 1.000)) {
			
			Double valorAtualizado = this.calculandoTransferenciaA(valorTransferido);
			transferencia.setValorTransferido(valorAtualizado);
		} else if((diferencaDeDias > 0 && diferencaDeDias <= 10) || 
				(valorTransferido > 1.000 && valorTransferido <= 2.000)) {
			
			Double valorAtualizado = this.calculandoTransferenciaB(valorTransferido);
			transferencia.setValorTransferido(valorAtualizado);
		} else if(diferencaDeDias > 10 || valorTransferido > 2.000) {
			
			Double valorAtualizado = this.calculandoTransferenciaC(valorTransferido, diferencaDeDias);
			transferencia.setValorTransferido(valorAtualizado);
		}
	}
	
	/**
	 *  Caso a transferencia seja feita no mesmo dia, ele irá diminuir o valor da conta origem
	 * 
	 * @param transferencia
	 * @author paulo
	 */
	private void extractedSubtracaoValorDaConta(Transferencia transferencia) {
		double reducaoValorDaConta = user.getConta().getValor() - transferencia.getValorTransferido();
		user.getConta().setValor(reducaoValorDaConta);
	}
	
}
