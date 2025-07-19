package com.vexa.vexa.controller;

import com.vexa.vexa.model.Tareas;
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.repository.EtiquetaRepository;
import com.vexa.vexa.repository.TareasRepository;
import com.vexa.vexa.repository.UsuarioRepository;
import com.vexa.vexa.service.TareasService;
import com.vexa.vexa.service.UsuarioService;
import com.vexa.vexa.model.Etiquetas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;

@Controller
public class TareaController {

    private final TareasService tareasService;
    private final UsuarioRepository usuarioRepository;
    private final TareasRepository tareasRepository;
    private final EtiquetaRepository etiquetaRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public TareaController(TareasService tareasService, UsuarioRepository usuarioRepository,
            TareasRepository tareasRepository, EtiquetaRepository etiquetaRepository, UsuarioService usuarioService) {
        this.tareasService = tareasService;
        this.usuarioRepository = usuarioRepository;
        this.etiquetaRepository = etiquetaRepository;
        this.tareasRepository = tareasRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/inicio")
    public String mostrarTareas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Tareas> listaTareas;
        if (usuario.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            listaTareas = tareasRepository.findByAdmin(usuario); // ✅ solo tareas que creó este admin
            model.addAttribute("usuariosHijos", usuarioRepository.findByAdmin(usuario)); // para asignar más tareas
        } else {
            listaTareas = tareasRepository.findByUsuario(usuario); // ✅ solo sus tareas
        }
        // 👈 etiquetas de la BDD
        model.addAttribute("tareas", listaTareas);

        model.addAttribute("tarea", new Tareas()); // para el formulario
        // 👇 Añade esto para mostrar etiquetas en el formulario
        List<Etiquetas> etiquetas = etiquetaRepository.findAll();
        model.addAttribute("etiquetas", etiquetas);
        return "inicio"; // Asegúrate de que el modal esté en inicio.html
    }

    @PostMapping("/tareas/editar")
    public String editarTarea(@RequestParam("id") Long tareaId,
        @RequestParam("titulo") String titulo,
        @RequestParam("descripcion") String descripcion,
        @RequestParam("usuario_id") Long usuarioId,
        @RequestParam("IdEtiqueta") Long etiquetaId) {
        Tareas tarea = tareasService.obtenerPorId(tareaId);
    if (tarea == null) {
        throw new RuntimeException("Tarea no encontrada");
    }

    Usuario usuarioAsignado = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    Etiquetas etiqueta = etiquetaRepository.findById(etiquetaId)
            .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));

    tarea.setTitulo(titulo);
    tarea.setDescripcion(descripcion);
    tarea.setUsuario(usuarioAsignado);
    tarea.setEtiquetas(etiqueta);
    // No tocar el estado ni checkList aquí si no lo estás editando
    tareasService.guardarTarea(tarea);

    return "redirect:/inicio";
    }
    @PostMapping("/tareas/eliminar")
public String eliminarTarea(@RequestParam("id") Long id) {
    tareasService.eliminarPorId(id);
    return "redirect:/inicio"; // o donde estés mostrando la lista de tareas
}

@PostMapping("/tareas/{id}/estado")
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
        return "redirect:/inicio";
    }

    @PostMapping("/guardar/tarea")
    public String guardarTarea(@ModelAttribute Tareas tarea,
            @RequestParam("usuario_id") Long usuarioId,
            @RequestParam("IdEtiqueta") Long etiquetaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();

        Usuario admin = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        Usuario usuarioAsignado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Etiquetas etiqueta = etiquetaRepository.findById(etiquetaId)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
        // 👇 Faltaba esto
        tarea.setAdmin(admin);
        tarea.setUsuario(usuarioAsignado);
        tarea.setEtiquetas(etiqueta);
        // ✅ Asignar por defecto si no llega del formulario
        tarea.setEstado("pendiente");
        tarea.setCheckList(false);
        tareasService.guardarTarea(tarea);

        return "redirect:/inicio";
    }

}
