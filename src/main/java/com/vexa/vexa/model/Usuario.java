package com.vexa.vexa.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuario_id;
    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    private String correo;
    private String contrasena;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Tareas> tareas;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "admin_id") // o "padre_id"
    private Usuario admin; // el padre que lo creó
    @OneToMany(mappedBy = "admin")
    @ToString.Exclude
    private List<Usuario> hijos;
    @Column(name = "contrasena_visible")
private String contrasenaVisible;

}
