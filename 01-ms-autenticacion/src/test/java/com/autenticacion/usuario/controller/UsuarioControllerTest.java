package com.autenticacion.usuario.controller;

import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Rol;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.security.JwtUtil;
import com.autenticacion.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Usuarios")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Listar - retorna lista de usuarios")
    void listar_retornaListaUsuarios() throws Exception {
        listadoDTO dto = new listadoDTO();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEmail("juan@test.com");
        dto.setDireccion("Calle 123");

        when(usuarioService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@test.com"))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @DisplayName("Agregar - email nuevo retorna 201")
    void agregar_emailNuevo_retorna201() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Ana");
        nuevo.setApellido("García");
        nuevo.setEmail("ana@test.com");
        nuevo.setContraseña("pass123");
        nuevo.setDireccion("Av. 100");
        nuevo.setRol(Rol.CLIENTE);

        listadoDTO dto = new listadoDTO();
        dto.setNombre("Ana");
        dto.setEmail("ana@test.com");

        when(usuarioService.existePorEmail("ana@test.com")).thenReturn(false);
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(dto);

        mockMvc.perform(post("/usuarios/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }

    @Test
    @DisplayName("Agregar - email duplicado retorna 409")
    void agregar_emailDuplicado_retorna409() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Ana");
        nuevo.setApellido("García");
        nuevo.setEmail("ana@test.com");
        nuevo.setContraseña("pass123");
        nuevo.setDireccion("Av. 100");
        nuevo.setRol(Rol.CLIENTE);

        when(usuarioService.existePorEmail("ana@test.com")).thenReturn(true);

        mockMvc.perform(post("/usuarios/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Eliminar - usuario existente retorna 200")
    void eliminar_usuarioExistente_retorna200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("juan@test.com");

        when(usuarioService.buscarPorId(1)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).eliminarUsuario(1);

        mockMvc.perform(delete("/usuarios/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado correctamente"));
    }

    @Test
    @DisplayName("Eliminar - usuario inexistente retorna 404")
    void eliminar_usuarioInexistente_retorna404() throws Exception {
        when(usuarioService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/usuarios/eliminar/99"))
                .andExpect(status().isNotFound());
    }
}
