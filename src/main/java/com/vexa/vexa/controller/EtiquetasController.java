package com.vexa.vexa.controller;

import com.vexa.vexa.model.Etiquetas;
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.service.EtiquetaService;
import com.vexa.vexa.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EtiquetasController {

    @Autowired
    private EtiquetaService etiquetaService;
    @Autowired
    private UsuarioService usuarioService;
    @GetMapping("/etiquetas")
    public String mostrarEtiquetas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correo = auth.getName();

    Usuario admin = usuarioService.obtenerPorCorreo(correo);
    List<Etiquetas> etiquetas = etiquetaService.listarPorAdmin(admin);
    
    model.addAttribute("etiquetas", etiquetas);
    return "etiquetas";
    }

    @PostMapping("/etiquetas/editar")
public String editarEtiqueta(@ModelAttribute Etiquetas etiqueta) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correo = auth.getName();
    Usuario admin = usuarioService.obtenerPorCorreo(correo);

    etiqueta.setAdmin(admin); // 🔴 ¡IMPORTANTE! Reasignar el usuario al editar
    etiquetaService.guardarEtiqueta(etiqueta); // reutilizamos el método guardar
    return "redirect:/etiquetas";
}



    @PostMapping("/etiquetas/eliminar")
    public String EliminarEtiqueta(@RequestParam("id_etiqueta") Long id) {
        etiquetaService.EliminarEtiqueta(id);

        return "redirect:/etiquetas";
    }
    @PostMapping("/guardar")
public String guardarEtiqueta(@ModelAttribute Etiquetas etiqueta) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correo = auth.getName();

    Usuario admin = usuarioService.obtenerPorCorreo(correo);
    etiqueta.setAdmin(admin); // Asignamos el admin actual como dueño de la etiqueta

    etiquetaService.guardarEtiqueta(etiqueta);
    return "redirect:/etiquetas"; // Redireccionamos a la página de etiquetas después de guardar
}


}
