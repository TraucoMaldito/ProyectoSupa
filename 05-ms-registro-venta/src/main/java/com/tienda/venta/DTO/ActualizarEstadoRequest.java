package com.tienda.venta.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActualizarEstadoRequest {
    @NotBlank(message = "El estado no puede estar vacio")
    private String estado;
}
