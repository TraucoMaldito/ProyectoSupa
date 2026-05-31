package com.tienda.certificacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String tipo;

    @NotNull
    @Column(name = "referencia_id", nullable = false)
    private Integer referenciaId;

    @NotNull
    @Column(name = "operador_id", nullable = false)
    private Integer operadorId;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String estado = "VIGENTE";

    @Column(name = "codigo_verificacion", unique = true, length = 36)
    private String codigoVerificacion = UUID.randomUUID().toString().substring(0, 12).toUpperCase();
}
