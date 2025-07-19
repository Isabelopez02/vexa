package com.vexa.vexa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "etiquetas")
public class Etiquetas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etiqueta") // Opcional si el nombre de la columna coincide
    private Long id_etiqueta;
    @Column(nullable = false)
    private String nombre;
    private String descripcion;
    private String color;
    private String icono;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Usuario admin;
}
