package com.tienda.catalogo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Producto disponible en el catálogo de la tienda")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Schema(description = "Identificador único del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Nombre del producto", example = "Laptop Gaming Pro")
    @NotBlank(message = "El nombre no puede quedar vacio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Laptop de alto rendimiento con procesador Intel i7 y 16GB RAM")
    @Column(length = 500)
    private String descripcion;

    @Schema(description = "Precio unitario del producto en CLP", example = "999990.00")
    @NotNull(message = "El precio no puede quedar vacio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Schema(description = "Cantidad disponible en inventario", example = "15")
    @NotNull(message = "El stock no puede quedar vacio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Schema(description = "Indica si el producto está activo y disponible para compra", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false)
    private Boolean activo = true;

    @Schema(description = "Categoría a la que pertenece el producto")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
