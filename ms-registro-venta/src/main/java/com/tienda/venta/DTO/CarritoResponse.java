package com.tienda.venta.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CarritoResponse {
    private Integer id;
    private Integer clienteId;
    private String estado;
    private List<ItemResponse> items;

    @Data
    public static class ItemResponse {
        private Integer id;
        private Integer productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}
