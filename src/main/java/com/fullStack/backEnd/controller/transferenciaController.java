package com.fullStack.backEnd.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fullStack.backEnd.dto.TransferenciaDTO;
import com.fullStack.backEnd.model.Conta;
import com.fullStack.backEnd.model.Transferencia;
import com.fullStack.backEnd.model.Usuario;
import com.fullStack.backEnd.repository.TransferenciaRepository;
import com.fullStack.backEnd.service.ContaService;
import com.fullStack.backEnd.service.TransferenciaService;
import com.fullStack.backEnd.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/transferencia")
@CrossOrigin("http://localhost:4200/")
public class TransferenciaController {

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
	public Transferencia efetuandoTransferencia(@RequestBody @Valid Transferencia transferencia) {
		
		if(transferencia.getValorTransferido() > 0 && null != transferencia.getContaOrigem()) {
			user = new Usuario();
			transferencia.setDataAgendamento(LocalDate.now());

			// Não estava conseguindo trabalhar com o LocalDate no Angular, então como estou com pouco tempo, decidi converter de String para LocalDate
			LocalDate dateTransferencia = LocalDate.parse(transferencia.getDataTransferencia());
			
			Conta contaOrigem = contaService.findByConta(transferencia.getContaOrigem());

			List<Usuario> listObjUsuario = userService.findAll();
			
			listObjUsuario.stream()
							.filter(x -> x.getConta().getId() == contaOrigem.getId())
							.forEach(x -> {
								user = x;
							});
			
			transferencia.setUsuario(user);
			
			long diferencaDeDias = transferencia.getDataAgendamento().until(dateTransferencia, ChronoUnit.DAYS);
			
			double valorTransferido = transferencia.getValorTransferido();
			
			if(dateTransferencia.isEqual(transferencia.getDataAgendamento()) ||
					(valorTransferido > 0 && valorTransferido <= 1.000)) {
				
				Double valorAtualizado = transferenciaService.calculandoTransferenciaA(valorTransferido);
				transferencia.setValorTransferido(valorAtualizado);
			} else if((diferencaDeDias > 0 && diferencaDeDias <= 10) || 
					(valorTransferido > 1.000 && valorTransferido <= 2.000)) {
				
				Double valorAtualizado = transferenciaService.calculandoTransferenciaB(valorTransferido);
				transferencia.setValorTransferido(valorAtualizado);
			} else if(diferencaDeDias > 10 || valorTransferido > 2.000) {
				
				Double valorAtualizado = transferenciaService.calculandoTransferenciaC(valorTransferido, diferencaDeDias);
				transferencia.setValorTransferido(valorAtualizado);
			}
			
			if(user.getConta().getValor() >= transferencia.getValorTransferido()) {
				if(dateTransferencia.isEqual(transferencia.getDataAgendamento())){
					extractedSubtracaoValorDaConta(transferencia);
					userService.saveR(user);
				}
				transferencia = transferenciaService.insert(transferencia);
			}
		}
		
		return transferencia;
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
	
	@GetMapping(value = "/todasTransferencias")
	public ResponseEntity<List<TransferenciaDTO>> buscandoTodasAsTransferencias() {
		List<Transferencia> listTransferencia = transfeRepository.findAll();
		List<TransferenciaDTO> listDto = listTransferencia.stream().map(obj -> new TransferenciaDTO(obj))
											.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
}
