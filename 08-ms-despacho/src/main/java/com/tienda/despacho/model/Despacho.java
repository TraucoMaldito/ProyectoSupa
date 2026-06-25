package com.tienda.despacho.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Orden de despacho desde una sucursal hacia el cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "despachos")
public class Despacho {

    @Schema(description = "Identificador único del despacho", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "ID de la venta asociada a este despacho", example = "1")
    @NotNull
    @Column(name = "venta_id", nullable = false)
    private Integer ventaId;

    @Schema(description = "ID de la sucursal que realiza el despacho", example = "2")
    @NotNull
    @Column(name = "sucursal_id", nullable = false)
    private Integer sucursalId;

    @Schema(description = "ID del operador encargado del despacho", example = "2")
    @NotNull
    @Column(name = "operador_id", nullable = false)
    private Integer operadorId;

    @Schema(description = "Estado actual del despacho", example = "EN_PREPARACION", allowableValues = {"EN_PREPARACION", "LISTO", "DESPACHADO", "ENTREGADO"}, accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false, length = 30)
    private String estado = "EN_PREPARACION";

    @Schema(description = "Fecha y hora en que se realizó el despacho", example = "2024-06-08T14:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;

    @Schema(description = "Fecha y hora de creación del registro", example = "2024-06-07T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Schema(description = "Observaciones adicionales sobre el despacho", example = "Entregar solo en horario de oficina")
    @Column(length = 500)
    private String observaciones;
}
