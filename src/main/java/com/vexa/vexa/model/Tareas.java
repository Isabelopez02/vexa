
package com.vexa.vexa.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor // Importante para que JPA pueda instanciarla
@Entity
@Table(name = "tareas") // Opcional, puedes cambiar el nombre si quieres
public class Tareas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;
    private LocalDate fechaCreacion;
    private LocalDate fechaLimite;
    private boolean checkList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etiqueta_id")
    private Etiquetas etiquetas;
    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "admin_id", nullable = false)
@ToString.Exclude
private Usuario admin;


}
