package com.tienda.carrito.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoResponse {
    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}
