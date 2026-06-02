package com.tienda.venta.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComprarRequest {

    @NotNull(message = "El clienteId es obligatorio")
    private Integer clienteId;
}
