package com.fullStack.backEnd.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fullStack.backEnd.model.Transferencia;
import com.fullStack.backEnd.service.TransferenciaService;

@RestController
@RequestMapping(value = "/api/transferencia")
@CrossOrigin("http://localhost:4200/")
public class TransferenciaController {

	@Autowired
	private TransferenciaService transferenciaService;
	
	private Transferencia transferenciaFinal;
	
	/**
	 * Efetua a transferencia já aplicando as regras de acordo com o valor e a data inserida
	 * 
	 * @author paulo
	 * @param transferencia
	 * @return
	 */
	@PostMapping(value = "/efetuandoTransferencia")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Transferencia> efetuandoTransferencia(@RequestBody @Valid Transferencia transferencia) {
		if(transferencia.getValorTransferido() > 0 && null != transferencia.getContaOrigem()) {
			transferenciaFinal = transferenciaService.processandoTransferencia(transferencia);
		} else {
			return new ResponseEntity<Transferencia>(transferencia, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Transferencia>(transferenciaFinal, HttpStatus.OK);
	}
}
