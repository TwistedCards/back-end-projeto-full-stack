package com.fullStack.backEnd.service;

import java.util.List;
import java.util.Optional;

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
	
	public Usuario saveR(Usuario obj) {
		obj = repository.save(obj);
		return obj;
	}
	
	public List<Usuario> findAll() {
		return repository.findAll();
	}
	
	public Optional<Usuario> findById(Long id) {
		return repository.findById(id);
	}
	
}
