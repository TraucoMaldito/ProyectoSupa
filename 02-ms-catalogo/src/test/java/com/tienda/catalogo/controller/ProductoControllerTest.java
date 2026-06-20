package com.tienda.catalogo.controller;

import com.tienda.catalogo.DTO.ProductoDTO;
import com.tienda.catalogo.model.Categoria;
import com.tienda.catalogo.model.Producto;
import com.tienda.catalogo.security.JwtUtil;
import com.tienda.catalogo.service.ProductoService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Productos")
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Listar - retorna productos activos")
    void listar_retornaProductosActivos() throws Exception {
        ProductoDTO dto = new ProductoDTO(1, "Laptop", "Gamer", BigDecimal.valueOf(999), 10, true, "Electrónica");

        when(productoService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[0].stock").value(10));
    }

    @Test
    @DisplayName("Buscar por ID - producto existente retorna 200")
    void buscarPorId_productoExistente_retorna200() throws Exception {
        ProductoDTO dto = new ProductoDTO(1, "Laptop", "Gamer", BigDecimal.valueOf(999), 10, true, "Electrónica");

        when(productoService.buscarPorId(1)).thenReturn(dto);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }

    @Test
    @DisplayName("Buscar por ID - producto inexistente retorna 404")
    void buscarPorId_productoInexistente_retorna404() throws Exception {
        when(productoService.buscarPorId(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Crear - producto válido retorna 201")
    void crear_productoValido_retorna201() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNombre("Electrónica");

        Producto producto = new Producto();
        producto.setNombre("Laptop");
        producto.setDescripcion("Gamer");
        producto.setPrecio(BigDecimal.valueOf(999));
        producto.setStock(10);
        producto.setCategoria(cat);

        ProductoDTO dto = new ProductoDTO(1, "Laptop", "Gamer", BigDecimal.valueOf(999), 10, true, "Electrónica");

        when(productoService.guardar(any(Producto.class))).thenReturn(dto);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }

    @Test
    @DisplayName("Eliminar - producto existente retorna 200")
    void eliminar_productoExistente_retorna200() throws Exception {
        doNothing().when(productoService).eliminar(1);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto desactivado correctamente"));
    }

    @Test
    @DisplayName("Listar por categoría - retorna productos de la categoría")
    void listarPorCategoria_retornaProductosDeLaCategoria() throws Exception {
        ProductoDTO dto = new ProductoDTO(1, "Laptop", "Gamer", BigDecimal.valueOf(999), 10, true, "Electrónica");

        when(productoService.listarPorCategoria(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[0].categoriaNombre").value("Electrónica"));
    }

    @Test
    @DisplayName("Actualizar - producto existente retorna 200")
    void actualizar_productoExistente_retorna200() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNombre("Electrónica");

        Producto datos = new Producto();
        datos.setNombre("Laptop Pro");
        datos.setDescripcion("Gaming Pro");
        datos.setPrecio(BigDecimal.valueOf(1299));
        datos.setStock(5);
        datos.setCategoria(cat);

        ProductoDTO dto = new ProductoDTO(1, "Laptop Pro", "Gaming Pro", BigDecimal.valueOf(1299), 5, true, "Electrónica");

        when(productoService.actualizar(eq(1), any(Producto.class))).thenReturn(dto);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop Pro"))
                .andExpect(jsonPath("$.precio").value(1299));
    }

    @Test
    @DisplayName("Reducir stock - cantidad válida retorna 200")
    void reducirStock_cantidadValida_retorna200() throws Exception {
        doNothing().when(productoService).reducirStock(1, 3);

        mockMvc.perform(put("/productos/1/stock")
                        .param("cantidad", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock actualizado"));
    }
}
