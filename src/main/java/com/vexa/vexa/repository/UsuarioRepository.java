package com.vexa.vexa.repository;

import com.vexa.vexa.model.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
Optional<Usuario> findByNombreUsuario(String username);
Optional<Usuario> findByCorreo(String correo);
List<Usuario> findByAdmin(Usuario admin);

}
