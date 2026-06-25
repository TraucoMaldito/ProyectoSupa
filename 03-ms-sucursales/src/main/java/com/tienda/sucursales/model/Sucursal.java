package com.tienda.sucursales.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Sucursal física de la tienda")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sucursales")
public class Sucursal {

    @Schema(description = "Identificador único de la sucursal", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro Santiago")
    @NotBlank(message = "El nombre no puede quedar vacio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Schema(description = "Dirección física de la sucursal", example = "Av. Libertador Bernardo O'Higgins 1234")
    @NotBlank(message = "La direccion no puede quedar vacia")
    @Column(nullable = false, length = 200)
    private String direccion;

    @Schema(description = "Ciudad donde se ubica la sucursal", example = "Santiago")
    @NotBlank(message = "La ciudad no puede quedar vacia")
    @Column(nullable = false, length = 50)
    private String ciudad;

    @Schema(description = "Número de teléfono de contacto", example = "+56 2 2345 6789")
    @Column(length = 20)
    private String telefono;

    @Schema(description = "Hora de apertura de la sucursal", example = "09:00")
    @Column(name = "horario_apertura", length = 10)
    private String horarioApertura;

    @Schema(description = "Hora de cierre de la sucursal", example = "20:00")
    @Column(name = "horario_cierre", length = 10)
    private String horarioCierre;

    @Schema(description = "Indica si la sucursal está operativa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false)
    private Boolean activa = true;
}
