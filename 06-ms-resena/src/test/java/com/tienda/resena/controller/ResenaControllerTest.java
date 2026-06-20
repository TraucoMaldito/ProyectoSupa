package com.tienda.resena.controller;

import com.tienda.resena.model.Resena;
import com.tienda.resena.security.JwtUtil;
import com.tienda.resena.service.ResenaService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResenaController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Reseñas")
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResenaService resenaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Resena resenaBase() {
        Resena resena = new Resena();
        resena.setId(1);
        resena.setClienteId(10);
        resena.setProductoId(5);
        resena.setCalificacion(5);
        resena.setComentario("Excelente producto");
        return resena;
    }

    @Test
    @DisplayName("Crear - reseña válida retorna 201")
    void crear_resenaValida_retorna201() throws Exception {
        Resena nueva = resenaBase();
        nueva.setId(null);

        when(resenaService.crear(any(Resena.class))).thenReturn(resenaBase());

        mockMvc.perform(post("/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.calificacion").value(5))
                .andExpect(jsonPath("$.comentario").value("Excelente producto"));
    }

    @Test
    @DisplayName("Crear - sin compra previa retorna 403")
    void crear_sinCompraPrevia_retorna403() throws Exception {
        Resena nueva = resenaBase();
        nueva.setId(null);

        when(resenaService.crear(any(Resena.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "No ha comprado el producto"));

        mockMvc.perform(post("/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Crear - reseña duplicada retorna 409")
    void crear_resenaDuplicada_retorna409() throws Exception {
        Resena nueva = resenaBase();
        nueva.setId(null);

        when(resenaService.crear(any(Resena.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Reseña duplicada"));

        mockMvc.perform(post("/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Listar por producto - retorna reseñas")
    void listarPorProducto_retornaResenas() throws Exception {
        when(resenaService.listarPorProducto(5)).thenReturn(List.of(resenaBase()));

        mockMvc.perform(get("/resenas/producto/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(5))
                .andExpect(jsonPath("$[0].calificacion").value(5));
    }

    @Test
    @DisplayName("Listar por cliente - retorna reseñas")
    void listarPorCliente_retornaResenas() throws Exception {
        when(resenaService.listarPorCliente(10)).thenReturn(List.of(resenaBase()));

        mockMvc.perform(get("/resenas/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(10));
    }

    @Test
    @DisplayName("Eliminar - reseña existente retorna 200")
    void eliminar_resenaExistente_retorna200() throws Exception {
        doNothing().when(resenaService).eliminar(1);

        mockMvc.perform(delete("/resenas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Resena eliminada correctamente"));
    }
}
