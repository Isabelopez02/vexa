package com.vexa.vexa.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vexa.vexa.model.Role;
import com.vexa.vexa.model.Usuario;
import com.vexa.vexa.repository.UsuarioRepository;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService{
    
    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByCorreo(correo)
        .orElseThrow(() -> new UsernameNotFoundException("Correo no encontrado"));

    return org.springframework.security.core.userdetails.User
        .withUsername(usuario.getCorreo()) // o .getNombreUsuario() si prefieres
        .password(usuario.getContrasena())
        .authorities(usuario.getRoles().stream().map(Role::getName).toArray(String[]::new))
        .build();
}

}
