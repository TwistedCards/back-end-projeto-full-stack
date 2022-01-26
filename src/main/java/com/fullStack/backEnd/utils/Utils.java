package com.fullStack.backEnd.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fullStack.backEnd.model.Conta;
import com.fullStack.backEnd.model.Usuario;
import com.fullStack.backEnd.service.ContaService;
import com.fullStack.backEnd.service.UsuarioService;

@Component
public class Utils {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ContaService contaService;

	/**
	 * Esse metodo já deixa alguns dados pré salvos toda vez que sobe a aplicação
	 * 
	 * @author paulo
	 */
	@PostConstruct
	public void preSalvamentoDados() {
		Conta conta1 = new Conta("45812719-2", "0351", 100000);
		contaService.save(conta1);
		
		Usuario user1 = new Usuario("Usuario 1", "607.260.970-80", conta1);
		usuarioService.save(user1);
		
		Conta conta2 = new Conta("43935431-3", "1785", 150000);
		contaService.save(conta2);
		
		Usuario user2 = new Usuario("Usuario 2", "768.465.510-85", conta2);
		usuarioService.save(user2);
	}
}
