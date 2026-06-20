package com.tienda.catalogo.controller;

import com.tienda.catalogo.model.Categoria;
import com.tienda.catalogo.security.JwtUtil;
import com.tienda.catalogo.service.CategoriaService;
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

@WebMvcTest(CategoriaController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Categorías")
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Listar - retorna lista de categorías")
    void listar_retornaListaCategorias() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNombre("Electrónica");

        when(categoriaService.listar()).thenReturn(List.of(cat));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Electrónica"));
    }

    @Test
    @DisplayName("Buscar por ID - categoría existente retorna 200")
    void buscarPorId_categoriaExistente_retorna200() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNombre("Electrónica");

        when(categoriaService.buscarPorId(1)).thenReturn(cat);

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Electrónica"));
    }

    @Test
    @DisplayName("Buscar por ID - categoría inexistente retorna 404")
    void buscarPorId_categoriaInexistente_retorna404() throws Exception {
        when(categoriaService.buscarPorId(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrada"));

        mockMvc.perform(get("/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Crear - categoría válida retorna 201")
    void crear_categoriaValida_retorna201() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setNombre("Ropa");

        Categoria guardada = new Categoria();
        guardada.setId(2);
        guardada.setNombre("Ropa");

        when(categoriaService.guardar(any(Categoria.class))).thenReturn(guardada);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ropa"));
    }

    @Test
    @DisplayName("Crear - nombre duplicado retorna 409")
    void crear_nombreDuplicado_retorna409() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setNombre("Electrónica");

        when(categoriaService.guardar(any(Categoria.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Nombre duplicado"));

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Actualizar - categoría existente retorna 200")
    void actualizar_categoriaExistente_retorna200() throws Exception {
        Categoria datos = new Categoria();
        datos.setNombre("Tecnología");

        Categoria actualizada = new Categoria();
        actualizada.setId(1);
        actualizada.setNombre("Tecnología");

        when(categoriaService.actualizar(eq(1), any(Categoria.class))).thenReturn(actualizada);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tecnología"));
    }

    @Test
    @DisplayName("Eliminar - categoría existente retorna 200")
    void eliminar_categoriaExistente_retorna200() throws Exception {
        doNothing().when(categoriaService).eliminar(1);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Categoria eliminada correctamente"));
    }
}
