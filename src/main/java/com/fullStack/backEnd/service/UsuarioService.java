package com.fullStack.backEnd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullStack.backEnd.model.Usuario;
import com.fullStack.backEnd.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	public Usuario save(Usuario obj) {
		obj.setId(null);
		obj = repository.save(obj);
		return obj;
	}
}
