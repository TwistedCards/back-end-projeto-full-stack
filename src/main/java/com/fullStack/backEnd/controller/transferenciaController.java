package com.fullStack.backEnd.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fullStack.backEnd.model.Conta;
import com.fullStack.backEnd.model.Transferencia;
import com.fullStack.backEnd.model.Usuario;
import com.fullStack.backEnd.repository.TransferenciaRepository;
import com.fullStack.backEnd.service.ContaService;
import com.fullStack.backEnd.service.TransferenciaService;
import com.fullStack.backEnd.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/transferencia")
public class transferenciaController {

	@Autowired
	private UsuarioService userService;
	
	@Autowired
	private ContaService contaService;
	
	@Autowired
	private TransferenciaService transferenciaService;
	
	@Autowired
	private TransferenciaRepository transfeRepository;
	
	private Usuario user = new Usuario();
	
	/**
	 * Efetua a transferencia já aplicando as regras de acordo com o valor e a data inserida
	 * 
	 * @author paulo
	 * @param transferencia
	 * @return
	 */
	@PostMapping(value = "/efetuandoTransferencia")
	@ResponseStatus(HttpStatus.CREATED)
	public Transferencia efetuandoTransferencia(Transferencia transferencia) {
		if(transferencia.getValorTransferido() > 0) {
			user = new Usuario();
			Conta contaOrigem = contaService.findByConta(transferencia.getContaOrigem());

			List<Usuario> listObjUsuario = userService.findAll();
			
			listObjUsuario.stream()
							.filter(x -> x.getConta().getId() == contaOrigem.getId())
							.forEach(x -> {
								user = x;
							});
			
			transferencia.setUsuario(user);
			
			long diferencaDeDias = transferencia.getDataAgendamento()
										.until(transferencia.getDataTransferencia(), ChronoUnit.DAYS);
			
			double valorTransferido = transferencia.getValorTransferido();
			
			if(transferencia.getDataTransferencia().isEqual(transferencia.getDataAgendamento()) ||
					(valorTransferido > 0 && valorTransferido <= 1.000)) {
				
				Double valorAtualizado = transferenciaService.calculandoTransferenciaA(valorTransferido);
				transferencia.setValorTransferido(valorAtualizado);
			} else if((diferencaDeDias > 0 && diferencaDeDias <= 10) || 
					(valorTransferido > 1.000 && valorTransferido <= 2.000)) {
				
				Double valorAtualizado = transferenciaService.calculandoTransferenciaB(valorTransferido);
				transferencia.setValorTransferido(valorAtualizado);
			} else if(diferencaDeDias > 10 || valorTransferido > 2.000) {
				
				Double valorAtualizado = transferenciaService.calculandoTransferenciaC(valorTransferido,
																						diferencaDeDias);
				transferencia.setValorTransferido(valorAtualizado);
			}
			
			if(user.getConta().getValor() >= transferencia.getValorTransferido()) {
				extractedSubtracaoValorDaConta(transferencia);
				transferencia = transferenciaService.insert(transferencia);
				userService.saveR(user);
			}
		}
		
		return transferencia;
	}

	private void extractedSubtracaoValorDaConta(Transferencia transferencia) {
		double reducaoValorDaConta = user.getConta().getValor() - transferencia.getValorTransferido();
		user.getConta().setValor(reducaoValorDaConta);
	}

	@GetMapping(value = "/todasTransferencias/{id}")
	public ResponseEntity<List<Transferencia>> findAllTransferByIdUser(@PathVariable long id) {
		List<Transferencia> listObj = transfeRepository.findAll();
		List<Transferencia> listTransferencia = new ArrayList<>();
		
		listObj.stream().filter(x -> x.getUsuario().getId() == id).forEach(x -> {
			listTransferencia.add(x);
		});
		
		return ResponseEntity.ok().body(listTransferencia);
	}
	
}
