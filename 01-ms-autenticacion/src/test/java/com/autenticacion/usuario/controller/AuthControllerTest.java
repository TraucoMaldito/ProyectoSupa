package com.autenticacion.usuario.controller;

import com.autenticacion.usuario.DTO.LoginRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Autenticación")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Login - credenciales válidas retorna token JWT")
    void login_credencialesValidas_retornaTokenJwt() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");

        Usuario usuario = new Usuario();
        usuario.setEmail("admin@test.com");
        usuario.setRol(Rol.ADMIN);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioService.buscarPorEmail("admin@test.com")).thenReturn(usuario);
        when(jwtUtil.generateToken("admin@test.com", "ADMIN")).thenReturn("mock.jwt.token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"))
                .andExpect(jsonPath("$.rol").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("admin@test.com"));
    }

    @Test
    @DisplayName("Login - credenciales inválidas retorna 401")
    void login_credencialesInvalidas_retorna401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Registro - email nuevo retorna 201 con usuario")
    void registro_emailNuevo_retorna201ConUsuario() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("María");
        nuevo.setApellido("López");
        nuevo.setEmail("maria@test.com");
        nuevo.setContraseña("pass123");
        nuevo.setDireccion("Calle 456");
        nuevo.setRol(Rol.CLIENTE);

        listadoDTO dto = new listadoDTO();
        dto.setNombre("María");
        dto.setApellido("López");
        dto.setEmail("maria@test.com");
        dto.setDireccion("Calle 456");

        when(usuarioService.existePorEmail("maria@test.com")).thenReturn(false);
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(dto);

        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("maria@test.com"));
    }

    @Test
    @DisplayName("Registro - email duplicado retorna 409")
    void registro_emailDuplicado_retorna409() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Carlos");
        nuevo.setApellido("Ruiz");
        nuevo.setEmail("existente@test.com");
        nuevo.setContraseña("pass123");
        nuevo.setDireccion("Av. 789");
        nuevo.setRol(Rol.CLIENTE);

        when(usuarioService.existePorEmail("existente@test.com")).thenReturn(true);

        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isConflict());
    }
}
