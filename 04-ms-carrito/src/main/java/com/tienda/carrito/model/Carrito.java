package com.tienda.carrito.model;

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

@Schema(description = "Carrito de compras de un cliente")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carritos")
public class Carrito {

    @Schema(description = "Identificador único del carrito", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "ID del cliente propietario del carrito", example = "3")
    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Schema(description = "Estado del carrito", example = "ACTIVO", allowableValues = {"ACTIVO", "COMPLETADO", "CANCELADO"}, accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    @Schema(description = "Fecha y hora de creación del carrito", example = "2024-06-01T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Schema(description = "Lista de productos agregados al carrito")
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemCarrito> items = new ArrayList<>();

    @Schema(description = "Total calculado del carrito en CLP", example = "59990.00", accessMode = Schema.AccessMode.READ_ONLY)
    public BigDecimal getTotal() {
        return items.stream()
                .map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
