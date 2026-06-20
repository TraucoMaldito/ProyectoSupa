package com.tienda.venta.controller;

import com.tienda.venta.DTO.ActualizarEstadoRequest;
import com.tienda.venta.DTO.ComprarRequest;
import com.tienda.venta.model.Venta;
import com.tienda.venta.security.JwtUtil;
import com.tienda.venta.service.VentaService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Registro de Ventas")
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    private Venta ventaBase() {
        Venta venta = new Venta();
        venta.setId(1);
        venta.setClienteId(10);
        venta.setTotal(BigDecimal.valueOf(500));
        venta.setEstado("PENDIENTE");
        return venta;
    }

    @Test
    @DisplayName("Comprar - carrito con items retorna 201")
    void comprar_carritoConItems_retorna201() throws Exception {
        ComprarRequest request = new ComprarRequest();
        request.setClienteId(10);

        when(ventaService.crearVenta(10)).thenReturn(ventaBase());

        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value(10))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    @DisplayName("Listar - retorna todas las ventas")
    void listar_retornaTodasLasVentas() throws Exception {
        when(ventaService.listar()).thenReturn(List.of(ventaBase()));

        mockMvc.perform(get("/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Buscar por ID - venta existente retorna 200")
    void buscarPorId_ventaExistente_retorna200() throws Exception {
        when(ventaService.buscarPorId(1)).thenReturn(ventaBase());

        mockMvc.perform(get("/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(10));
    }

    @Test
    @DisplayName("Buscar por ID - venta inexistente retorna 404")
    void buscarPorId_ventaInexistente_retorna404() throws Exception {
        when(ventaService.buscarPorId(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrada"));

        mockMvc.perform(get("/ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Buscar por cliente - retorna ventas del cliente")
    void buscarPorCliente_retornaVentasDelCliente() throws Exception {
        when(ventaService.buscarPorCliente(10)).thenReturn(List.of(ventaBase()));

        mockMvc.perform(get("/ventas/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(10));
    }

    @Test
    @DisplayName("Actualizar estado - venta existente retorna 200")
    void actualizarEstado_ventaExistente_retorna200() throws Exception {
        ActualizarEstadoRequest request = new ActualizarEstadoRequest();
        request.setEstado("PAGADO");

        Venta actualizada = ventaBase();
        actualizada.setEstado("PAGADO");

        when(ventaService.actualizarEstado(eq(1), any(ActualizarEstadoRequest.class))).thenReturn(actualizada);

        mockMvc.perform(put("/ventas/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PAGADO"));
    }
}
