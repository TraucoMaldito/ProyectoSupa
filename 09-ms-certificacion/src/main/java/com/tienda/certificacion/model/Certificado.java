package com.tienda.certificacion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Certificado de autenticidad emitido para un despacho")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "certificados")
public class Certificado {

    @Schema(description = "Identificador único del certificado", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Tipo de certificado emitido", example = "DESPACHO", allowableValues = {"DESPACHO", "ENTREGA", "DEVOLUCION"})
    @NotBlank
    @Column(nullable = false, length = 30)
    private String tipo;

    @Schema(description = "ID del recurso referenciado (despacho, envío, etc.)", example = "1")
    @NotNull
    @Column(name = "referencia_id", nullable = false)
    private Integer referenciaId;

    @Schema(description = "ID del operador que emitió el certificado", example = "2")
    @NotNull
    @Column(name = "operador_id", nullable = false)
    private Integer operadorId;

    @Schema(description = "Fecha y hora de emisión del certificado", example = "2024-06-09T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Schema(description = "Descripción adicional del certificado", example = "Despacho verificado y entregado en domicilio del cliente")
    @Column(length = 500)
    private String descripcion;

    @Schema(description = "Estado del certificado", example = "VIGENTE", allowableValues = {"VIGENTE", "REVOCADO", "VENCIDO"}, accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false, length = 20)
    private String estado = "VIGENTE";

    @Schema(description = "Código único de verificación del certificado", example = "A1B2C3D4E5F6", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "codigo_verificacion", unique = true, length = 36)
    private String codigoVerificacion = UUID.randomUUID().toString().substring(0, 12).toUpperCase();
}
