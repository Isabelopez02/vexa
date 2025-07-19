package com.vexa.vexa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.vexa.vexa.model.Etiquetas;           // Ajusta el paquete
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.repository.EtiquetaRepository;  // Ajusta el paquete

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    public Etiquetas buscarPorId(Long id){
        return etiquetaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Id no encontrado"));
    }

    public List<Etiquetas> listarEtiquetas() {
        return etiquetaRepository.findAll();
    }

    public List<Etiquetas> listarPorAdmin(Usuario admin) {
    return etiquetaRepository.findByAdmin(admin);
}

    public void guardarEtiqueta(Etiquetas etiqueta) {
        etiquetaRepository.save(etiqueta);
    }
    public void EliminarEtiqueta(Long id){
        etiquetaRepository.deleteById(id);
    }
    
}

