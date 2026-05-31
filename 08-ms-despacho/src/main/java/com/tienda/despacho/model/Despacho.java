package com.tienda.despacho.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "despachos")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "venta_id", nullable = false)
    private Integer ventaId;

    @NotNull
    @Column(name = "sucursal_id", nullable = false)
    private Integer sucursalId;

    @NotNull
    @Column(name = "operador_id", nullable = false)
    private Integer operadorId;

    @Column(nullable = false, length = 30)
    private String estado = "EN_PREPARACION";

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(length = 500)
    private String observaciones;
}
