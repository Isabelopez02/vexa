package com.vexa.vexa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vexa.vexa.model.Tareas;
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.repository.TareasRepository;
import com.vexa.vexa.repository.UsuarioRepository;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TareaPendienteController {

    @Autowired
    private TareasRepository tareasRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @GetMapping("/tareas/pendiente")
public String MostrarPendientes(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correo = auth.getName();

    Usuario usuario = usuarioRepository.findByCorreo(correo)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    List<Tareas> tareaspendientes;

    if (usuario.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
        // El admin ve las tareas pendientes que él creó
        tareaspendientes = tareasRepository.findByAdminAndEstado(usuario, "pendiente");
    } else {
        // El usuario hijo ve solo sus tareas asignadas pendientes
        tareaspendientes = tareasRepository.findByUsuarioAndEstado(usuario, "pendiente");
    }

    model.addAttribute("pendientes", tareaspendientes);
    return "tareaspendiente";
}


    @PostMapping("/tareas/pendiente/{id}/estado")
    public String VerificarEstado(@PathVariable long id) {
        Optional<Tareas> verificarestado = tareasRepository.findById(id);
        if (verificarestado.isPresent()) {
            Tareas tarea = verificarestado.get();
            boolean nuevoEstado = !tarea.isCheckList();
            tarea.setCheckList(nuevoEstado);
            if (nuevoEstado) {
                    tarea.setCheckList(true);
                    tarea.setEstado("completado");
            } else {
                    tarea.setCheckList(false);
                    tarea.setEstado("pendiente");
            }
            tareasRepository.save(tarea);
    }
        return "redirect:/tareas/pendiente";
    }

}
