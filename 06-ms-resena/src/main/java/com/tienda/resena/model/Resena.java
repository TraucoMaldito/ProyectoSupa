package com.tienda.resena.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Reseña de un producto realizada por un cliente que lo compró")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resenas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cliente_id", "producto_id"})
})
public class Resena {

    @Schema(description = "Identificador único de la reseña", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "ID del cliente que escribió la reseña", example = "3")
    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Schema(description = "ID del producto reseñado", example = "5")
    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    @Schema(description = "Calificación del producto del 1 al 5", example = "5", minimum = "1", maximum = "5")
    @NotNull
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    @Column(nullable = false)
    private Integer calificacion;

    @Schema(description = "Comentario del cliente sobre el producto", example = "Excelente producto, llegó en perfectas condiciones y funciona perfecto.")
    @NotBlank(message = "El comentario no puede quedar vacio")
    @Column(nullable = false, length = 500)
    private String comentario;

    @Schema(description = "Fecha y hora en que se publicó la reseña", example = "2024-06-15T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
