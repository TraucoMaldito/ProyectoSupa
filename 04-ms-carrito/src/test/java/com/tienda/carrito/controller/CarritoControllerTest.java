package com.tienda.carrito.controller;

import com.tienda.carrito.DTO.AgregarItemRequest;
import com.tienda.carrito.model.Carrito;
import com.tienda.carrito.security.JwtUtil;
import com.tienda.carrito.service.CarritoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Carrito de Compras")
public class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService carritoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    private Carrito carritoActivo() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setClienteId(10);
        carrito.setEstado("ACTIVO");
        carrito.setItems(new ArrayList<>());
        return carrito;
    }

    @Test
    @DisplayName("Obtener - cliente con carrito retorna 200")
    void obtener_clienteConCarrito_retorna200() throws Exception {
        when(carritoService.obtenerCarrito(10)).thenReturn(carritoActivo());

        mockMvc.perform(get("/carrito/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(10))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    @DisplayName("Agregar - producto disponible retorna 200")
    void agregar_productoDisponible_retorna200() throws Exception {
        AgregarItemRequest request = new AgregarItemRequest();
        request.setProductoId(5);
        request.setCantidad(2);

        Carrito carrito = carritoActivo();
        when(carritoService.agregarItem(eq(10), any(AgregarItemRequest.class))).thenReturn(carrito);

        mockMvc.perform(post("/carrito/cliente/10/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(10));
    }

    @Test
    @DisplayName("Agregar - producto no disponible retorna 404")
    void agregar_productoNoDisponible_retorna404() throws Exception {
        AgregarItemRequest request = new AgregarItemRequest();
        request.setProductoId(999);
        request.setCantidad(1);

        when(carritoService.agregarItem(eq(10), any(AgregarItemRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no disponible"));

        mockMvc.perform(post("/carrito/cliente/10/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Eliminar item - item existente retorna 200")
    void eliminarItem_itemExistente_retorna200() throws Exception {
        doNothing().when(carritoService).eliminarItem(3);

        mockMvc.perform(delete("/carrito/item/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item eliminado del carrito"));
    }

    @Test
    @DisplayName("Vaciar - carrito activo retorna 200")
    void vaciar_carritoActivo_retorna200() throws Exception {
        doNothing().when(carritoService).vaciarCarrito(10);

        mockMvc.perform(delete("/carrito/cliente/10/vaciar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrito vaciado"));
    }

    @Test
    @DisplayName("Total items - retorna cantidad de items")
    void totalItems_retornaCantidad() throws Exception {
        when(carritoService.contarItems(10)).thenReturn(5);

        mockMvc.perform(get("/carrito/cliente/10/total-items"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
