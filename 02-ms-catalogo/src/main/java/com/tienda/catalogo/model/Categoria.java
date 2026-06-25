package com.tienda.catalogo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Categoría de productos del catálogo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {

    @Schema(description = "Identificador único de la categoría", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Nombre único de la categoría", example = "Electrónica")
    @NotBlank(message = "El nombre no puede quedar vacio")
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Schema(description = "Descripción detallada de la categoría", example = "Computadores, tablets, smartphones y accesorios tecnológicos")
    @Column(length = 200)
    private String descripcion;
}
