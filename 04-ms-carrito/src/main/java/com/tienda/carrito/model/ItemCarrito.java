package com.tienda.carrito.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Ítem individual dentro de un carrito de compras")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items_carrito")
public class ItemCarrito {

    @Schema(description = "Identificador único del ítem", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @Schema(description = "ID del producto agregado al carrito", example = "5")
    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    @Schema(description = "Nombre del producto al momento de agregarlo", example = "Laptop Gaming Pro", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "nombre_producto", length = 100)
    private String nombreProducto;

    @Schema(description = "Cantidad del producto solicitada", example = "2")
    @NotNull
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;

    @Schema(description = "Precio unitario del producto al momento de agregarlo", example = "999990.00", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
}
