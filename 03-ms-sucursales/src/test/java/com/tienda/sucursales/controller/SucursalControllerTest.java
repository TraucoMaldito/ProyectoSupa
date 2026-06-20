package com.tienda.sucursales.controller;

import com.tienda.sucursales.model.Sucursal;
import com.tienda.sucursales.security.JwtUtil;
import com.tienda.sucursales.service.SucursalService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Sucursales")
public class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SucursalService sucursalService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Listar - retorna sucursales activas")
    void listar_retornaSucursalesActivas() throws Exception {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal Centro");
        sucursal.setDireccion("Av. Principal 100");
        sucursal.setCiudad("Santiago");

        when(sucursalService.listar()).thenReturn(List.of(sucursal));

        mockMvc.perform(get("/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Sucursal Centro"))
                .andExpect(jsonPath("$[0].ciudad").value("Santiago"));
    }

    @Test
    @DisplayName("Buscar por ID - sucursal existente retorna 200")
    void buscarPorId_sucursalExistente_retorna200() throws Exception {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal Centro");

        when(sucursalService.buscarPorId(1)).thenReturn(sucursal);

        mockMvc.perform(get("/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro"));
    }

    @Test
    @DisplayName("Buscar por ID - sucursal inexistente retorna 404")
    void buscarPorId_sucursalInexistente_retorna404() throws Exception {
        when(sucursalService.buscarPorId(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrada"));

        mockMvc.perform(get("/sucursales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Crear - sucursal válida retorna 201")
    void crear_sucursalValida_retorna201() throws Exception {
        Sucursal nueva = new Sucursal();
        nueva.setNombre("Sucursal Norte");
        nueva.setDireccion("Calle 200");
        nueva.setCiudad("Valparaíso");

        Sucursal guardada = new Sucursal();
        guardada.setId(2);
        guardada.setNombre("Sucursal Norte");
        guardada.setCiudad("Valparaíso");

        when(sucursalService.guardar(any(Sucursal.class))).thenReturn(guardada);

        mockMvc.perform(post("/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sucursal Norte"));
    }

    @Test
    @DisplayName("Actualizar - sucursal existente retorna 200")
    void actualizar_sucursalExistente_retorna200() throws Exception {
        Sucursal datos = new Sucursal();
        datos.setNombre("Sucursal Centro Actualizada");
        datos.setDireccion("Av. Principal 200");
        datos.setCiudad("Santiago");

        Sucursal actualizada = new Sucursal();
        actualizada.setId(1);
        actualizada.setNombre("Sucursal Centro Actualizada");

        when(sucursalService.actualizar(eq(1), any(Sucursal.class))).thenReturn(actualizada);

        mockMvc.perform(put("/sucursales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro Actualizada"));
    }

    @Test
    @DisplayName("Eliminar - sucursal existente retorna 200")
    void eliminar_sucursalExistente_retorna200() throws Exception {
        doNothing().when(sucursalService).eliminar(1);

        mockMvc.perform(delete("/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sucursal desactivada correctamente"));
    }
}
