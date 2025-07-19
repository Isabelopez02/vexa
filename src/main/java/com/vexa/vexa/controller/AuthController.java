package com.vexa.vexa.controller;

import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.service.UsuarioService;
import com.vexa.vexa.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/log")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // Si llega aquí, está autenticado
            System.out.println("✅ Autenticación exitosa para: " + username);

            Usuario usuario = usuarioService.obtenerPorCorreo(username); // Asegúrate de tener este método
            String token = jwtUtil.generateToken(usuario.getCorreo());
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("🔐 JWT generado: " + token);

            // Por ahora, solo redirige a una página de bienvenida o dashboard
            return "redirect:/";

        } catch (AuthenticationException e) {
            System.out.println("❌ Falló la autenticación: " + e.getMessage());
            model.addAttribute("error", true);
            return "index"; // Vuelve al login.html con mensaje de error
        }
    }
}
