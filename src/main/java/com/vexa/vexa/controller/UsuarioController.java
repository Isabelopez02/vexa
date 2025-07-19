package com.vexa.vexa.controller;

import com.vexa.vexa.repository.RoleRepository;
import com.vexa.vexa.repository.UsuarioRepository;
import com.vexa.vexa.model.Role;
import com.vexa.vexa.model.Usuario;

import org.springframework.security.core.Authentication; // ✅ ESTE ES EL CORRECTO
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    UsuarioController(UsuarioRepository usuarioRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String paginaInicio(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @PostMapping("/registrar")
public String registrarUsuario(@ModelAttribute Usuario usuario) {
    try {
        System.out.println("📥 Registrando: " + usuario);

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        Role rolUsuario = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no existe"));

        usuario.getRoles().add(rolUsuario);

        usuarioRepository.save(usuario);

        System.out.println("✅ Registrado correctamente");

        return "redirect:/";
    } catch (Exception e) {
        e.printStackTrace(); // ⛔ Muestra el error exacto en consola
        return "error"; // puedes crear una página error.html para mostrar algo amigable
    }
}


    @GetMapping("/hijos")
    public String listarHijosDelAdmin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoAdmin = auth.getName();

        Usuario admin = usuarioRepository.findByCorreo(correoAdmin).orElseThrow();
        List<Usuario> hijos = usuarioRepository.findByAdmin(admin);

        model.addAttribute("hijos", hijos);
        return "hijosAdmin"; // Asegúrate de tener este HTML creado
    }

    @PostMapping("/hijos/registrar")
    public String registrarHijo(@ModelAttribute Usuario usuario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoAdmin = auth.getName();
        Usuario admin = usuarioRepository.findByCorreo(correoAdmin).orElseThrow();

        String originalPassword = usuario.getContrasena(); // 📌 guarda el texto plano
        usuario.setContrasena(passwordEncoder.encode(originalPassword)); // 🔐 guarda encriptado
        usuario.setContrasenaVisible(originalPassword); // 👁️ guarda visible

        Role rol = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("No se encontró el rol USER"));
        usuario.getRoles().add(rol);

        usuario.setAdmin(admin);
        usuarioRepository.save(usuario);
        return "redirect:/hijos";
    }

}
