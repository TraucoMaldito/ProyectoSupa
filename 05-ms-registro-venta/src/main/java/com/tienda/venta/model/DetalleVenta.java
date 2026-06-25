package com.tienda.venta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Línea de detalle de una venta con el producto y cantidad comprada")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "detalle_ventas")
public class DetalleVenta {

    @Schema(description = "Identificador único del detalle", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Schema(description = "ID del producto vendido", example = "5")
    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    @Schema(description = "Nombre del producto al momento de la venta", example = "Laptop Gaming Pro", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    @Column(length = 100)
    private String nombreProducto;

    @Schema(description = "Cantidad de unidades compradas", example = "2")
    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    @Schema(description = "Precio unitario del producto al momento de la venta", example = "999990.00", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
}
