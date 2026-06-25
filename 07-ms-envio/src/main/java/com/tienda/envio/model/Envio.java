package com.tienda.envio.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Registro de envío asociado a una venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Schema(description = "Identificador único del envío", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "ID de la venta asociada al envío", example = "1")
    @NotNull
    @Column(name = "venta_id", nullable = false)
    private Integer ventaId;

    @Schema(description = "ID del cliente destinatario del envío", example = "3")
    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Schema(description = "Dirección de destino del envío", example = "Av. Principal 456, Providencia, Santiago")
    @NotBlank
    @Column(name = "direccion_destino", nullable = false, length = 200)
    private String direccionDestino;

    @Schema(description = "Estado actual del envío", example = "PREPARANDO", allowableValues = {"PREPARANDO", "EN_CAMINO", "ENTREGADO", "DEVUELTO"}, accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false, length = 20)
    private String estado = "PREPARANDO";

    @Schema(description = "Fecha estimada de entrega", example = "2024-06-10")
    @Column(name = "fecha_estimada")
    private LocalDate fechaEstimada;

    @Schema(description = "Fecha real de entrega al cliente", example = "2024-06-09", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Schema(description = "Código único de seguimiento del envío", example = "ENV-001A", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "codigo_seguimiento", unique = true, length = 36)
    private String codigoSeguimiento = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    @Schema(description = "Fecha y hora de registro del envío", example = "2024-06-05T08:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
