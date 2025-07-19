package com.vexa.vexa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vexa.vexa.model.Tareas;
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.repository.TareasRepository;
import com.vexa.vexa.repository.UsuarioRepository;



@Controller
public class TareasCompletadas {

    @Autowired
    private TareasRepository tareasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/tareas/completadas")
    public String mostrarTareasCompletadas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Tareas> tareascompletadas;

        if (usuario.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            // ✅ El admin ve las tareas completadas que él creó
            tareascompletadas = tareasRepository.findByAdminAndEstado(usuario, "completado");
        } else {
            // ✅ El usuario hijo ve solo sus tareas asignadas completadas
            tareascompletadas = tareasRepository.findByUsuarioAndEstado(usuario, "completado");
        }

        model.addAttribute("completado", tareascompletadas);
        return "tareascompletadas";
    }
}
