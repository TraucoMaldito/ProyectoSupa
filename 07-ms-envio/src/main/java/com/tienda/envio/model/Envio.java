package com.tienda.envio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "venta_id", nullable = false)
    private Integer ventaId;

    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @NotBlank
    @Column(name = "direccion_destino", nullable = false, length = 200)
    private String direccionDestino;

    @Column(nullable = false, length = 20)
    private String estado = "PREPARANDO";

    @Column(name = "fecha_estimada")
    private LocalDate fechaEstimada;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "codigo_seguimiento", unique = true, length = 36)
    private String codigoSeguimiento = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
