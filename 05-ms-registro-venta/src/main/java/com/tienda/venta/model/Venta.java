package com.tienda.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Registro de una venta realizada en la tienda")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ventas")
public class Venta {

    @Schema(description = "Identificador único de la venta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "ID del cliente que realizó la compra", example = "3")
    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Schema(description = "Fecha y hora en que se registró la venta", example = "2024-06-01T14:25:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Schema(description = "Monto total de la venta en CLP", example = "1999980.00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Schema(description = "Estado actual de la venta", example = "PENDIENTE", allowableValues = {"PENDIENTE", "PAGADO", "ENVIADO", "ENTREGADO", "CANCELADO"})
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Schema(description = "Detalle de los productos incluidos en la venta")
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleVenta> detalles = new ArrayList<>();
}
