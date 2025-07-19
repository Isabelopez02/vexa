package com.vexa.vexa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vexa.vexa.model.Etiquetas;
import com.vexa.vexa.model.Usuario; 

public interface EtiquetaRepository extends JpaRepository<Etiquetas, Long> {

    List<Etiquetas> findByAdmin(Usuario admin);

}
