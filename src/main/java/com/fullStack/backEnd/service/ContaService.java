package com.fullStack.backEnd.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullStack.backEnd.model.Conta;
import com.fullStack.backEnd.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository repository;
	
	@Transactional
	public Conta save(Conta obj) {
		obj.setId(null);
		obj = repository.save(obj);
		return obj;
	}
	
	public Conta findByConta(String numeroConta) {
		return repository.findByNumeroConta(numeroConta);
	}
	
}
