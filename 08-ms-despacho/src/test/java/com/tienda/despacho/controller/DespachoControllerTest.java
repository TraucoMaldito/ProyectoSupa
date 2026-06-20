package com.tienda.despacho.controller;

import com.tienda.despacho.model.Despacho;
import com.tienda.despacho.security.JwtUtil;
import com.tienda.despacho.service.DespachoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DespachoController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Despachos")
public class DespachoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DespachoService despachoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Despacho despachoBase() {
        Despacho despacho = new Despacho();
        despacho.setId(1);
        despacho.setVentaId(20);
        despacho.setSucursalId(3);
        despacho.setOperadorId(7);
        despacho.setEstado("EN_PREPARACION");
        return despacho;
    }

    @Test
    @DisplayName("Crear - despacho válido retorna 201")
    void crear_despachoValido_retorna201() throws Exception {
        Despacho nuevo = new Despacho();
        nuevo.setVentaId(20);
        nuevo.setSucursalId(3);
        nuevo.setOperadorId(7);

        when(despachoService.crear(any(Despacho.class))).thenReturn(despachoBase());

        mockMvc.perform(post("/despachos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ventaId").value(20))
                .andExpect(jsonPath("$.estado").value("EN_PREPARACION"));
    }

    @Test
    @DisplayName("Crear - venta inexistente retorna 404")
    void crear_ventaInexistente_retorna404() throws Exception {
        Despacho nuevo = new Despacho();
        nuevo.setVentaId(999);
        nuevo.setSucursalId(3);
        nuevo.setOperadorId(7);

        when(despachoService.crear(any(Despacho.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        mockMvc.perform(post("/despachos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Buscar por ID - despacho existente retorna 200")
    void buscarPorId_despachoExistente_retorna200() throws Exception {
        when(despachoService.buscarPorId(1)).thenReturn(despachoBase());

        mockMvc.perform(get("/despachos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucursalId").value(3));
    }

    @Test
    @DisplayName("Buscar por ID - despacho inexistente retorna 404")
    void buscarPorId_despachoInexistente_retorna404() throws Exception {
        when(despachoService.buscarPorId(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        mockMvc.perform(get("/despachos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Listar por sucursal - retorna despachos")
    void listarPorSucursal_retornaDespachos() throws Exception {
        when(despachoService.listarPorSucursal(3)).thenReturn(List.of(despachoBase()));

        mockMvc.perform(get("/despachos/sucursal/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sucursalId").value(3));
    }

    @Test
    @DisplayName("Buscar por venta - retorna despacho")
    void buscarPorVenta_retornaDespacho() throws Exception {
        when(despachoService.buscarPorVenta(20)).thenReturn(despachoBase());

        mockMvc.perform(get("/despachos/venta/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ventaId").value(20));
    }

    @Test
    @DisplayName("Actualizar estado - retorna 200")
    void actualizarEstado_retorna200() throws Exception {
        Despacho actualizado = despachoBase();
        actualizado.setEstado("DESPACHADO");

        when(despachoService.actualizarEstado(1, "DESPACHADO")).thenReturn(actualizado);

        mockMvc.perform(put("/despachos/1/estado")
                        .param("estado", "DESPACHADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("DESPACHADO"));
    }
}
