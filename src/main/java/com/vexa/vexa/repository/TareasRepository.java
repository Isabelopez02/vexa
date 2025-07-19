package com.vexa.vexa.repository;

import com.vexa.vexa.model.Tareas;
import com.vexa.vexa.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TareasRepository extends JpaRepository<Tareas, Long> {
    List<Tareas> findByEstado(String estado);
    List<Tareas> findByUsuario(Usuario usuario);
    List<Tareas> findByAdmin(Usuario admin);
    List<Tareas> findByUsuarioAndEstado(Usuario usuario, String estado);

List<Tareas> findByAdminAndEstado(Usuario admin, String estado);


}