package com.vexa.vexa.service;

import com.vexa.vexa.model.Tareas;
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.repository.TareasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareasService {

    private final TareasRepository tareasRepository;
    public Tareas obtenerPorId(Long id) {
    return tareasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + id));
}

    public TareasService(TareasRepository tareasRepository) {
        this.tareasRepository = tareasRepository;
    }

    public List<Tareas> obtenerTodasLasTareas() {
        return tareasRepository.findAll();
    }

    public List<Tareas> obtenerTareasPorEstado(String estado) {
        return tareasRepository.findByEstado(estado);
    }

    public void guardarTarea(Tareas tarea) {
        tareasRepository.save(tarea);
    }

    public void eliminarPorId(Long id) {
        tareasRepository.deleteById(id);
    }

    public List<Tareas> obtenerTareasPorUsuario(Usuario usuario) {
        return tareasRepository.findByUsuario(usuario);
    }

    // Puedes agregar métodos para eliminar, actualizar, buscar, etc.
}
