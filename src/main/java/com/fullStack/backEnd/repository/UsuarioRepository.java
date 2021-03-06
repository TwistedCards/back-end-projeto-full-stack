package com.fullStack.backEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullStack.backEnd.model.Conta;
import com.fullStack.backEnd.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Usuario findByConta(Conta conta);
}
