package com.fullStack.backEnd.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullStack.backEnd.model.Transferencia;

@RestController
@RequestMapping(value = "/api/transferencia")
public class transferenciaController {

	@PostMapping(value = "/efetuandoTransferencia")
	public Transferencia efetuandoTransferencia(Transferencia transferencia) {
		if(transferencia.getValorTransferido() > 0) {
			
		}
		return transferencia;
	}
	
	
}
