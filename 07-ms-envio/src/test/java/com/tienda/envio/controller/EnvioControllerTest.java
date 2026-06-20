package com.tienda.envio.controller;

import com.tienda.envio.model.Envio;
import com.tienda.envio.security.JwtUtil;
import com.tienda.envio.service.EnvioService;
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

@WebMvcTest(EnvioController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Envíos")
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioService envioService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Envio envioBase() {
        Envio envio = new Envio();
        envio.setId(1);
        envio.setVentaId(20);
        envio.setClienteId(10);
        envio.setDireccionDestino("Calle 123, Santiago");
        envio.setEstado("PREPARANDO");
        envio.setCodigoSeguimiento("ENV-20-001");
        return envio;
    }

    @Test
    @DisplayName("Crear - envío válido retorna 201")
    void crear_envioValido_retorna201() throws Exception {
        Envio nuevo = new Envio();
        nuevo.setVentaId(20);
        nuevo.setClienteId(10);
        nuevo.setDireccionDestino("Calle 123, Santiago");

        when(envioService.crear(any(Envio.class))).thenReturn(envioBase());

        mockMvc.perform(post("/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ventaId").value(20))
                .andExpect(jsonPath("$.codigoSeguimiento").value("ENV-20-001"));
    }

    @Test
    @DisplayName("Crear - envío ya existe retorna 409")
    void crear_envioYaExiste_retorna409() throws Exception {
        Envio nuevo = new Envio();
        nuevo.setVentaId(20);
        nuevo.setClienteId(10);
        nuevo.setDireccionDestino("Calle 123");

        when(envioService.crear(any(Envio.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un envío para esta venta"));

        mockMvc.perform(post("/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Buscar por ID - envío existente retorna 200")
    void buscarPorId_envioExistente_retorna200() throws Exception {
        when(envioService.buscarPorId(1)).thenReturn(envioBase());

        mockMvc.perform(get("/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoSeguimiento").value("ENV-20-001"));
    }

    @Test
    @DisplayName("Buscar por venta - retorna envío")
    void buscarPorVenta_retornaEnvio() throws Exception {
        when(envioService.buscarPorVenta(20)).thenReturn(envioBase());

        mockMvc.perform(get("/envios/venta/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ventaId").value(20));
    }

    @Test
    @DisplayName("Buscar por código - código válido retorna envío")
    void buscarPorCodigo_codigoValido_retornaEnvio() throws Exception {
        when(envioService.buscarPorCodigo("ENV-20-001")).thenReturn(envioBase());

        mockMvc.perform(get("/envios/seguimiento/ENV-20-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(10));
    }

    @Test
    @DisplayName("Listar por cliente - retorna envíos")
    void listarPorCliente_retornaEnvios() throws Exception {
        when(envioService.listarPorCliente(10)).thenReturn(List.of(envioBase()));

        mockMvc.perform(get("/envios/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(10));
    }

    @Test
    @DisplayName("Actualizar estado - retorna 200")
    void actualizarEstado_retorna200() throws Exception {
        Envio actualizado = envioBase();
        actualizado.setEstado("EN_CAMINO");

        when(envioService.actualizarEstado(1, "EN_CAMINO")).thenReturn(actualizado);

        mockMvc.perform(put("/envios/1/estado")
                        .param("estado", "EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_CAMINO"));
    }
}
