package com.autenticacion.usuario.controller;

import com.autenticacion.usuario.DTO.LoginRequest;
import com.autenticacion.usuario.DTO.LoginResponse;
import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.security.JwtUtil;
import com.autenticacion.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Endpoints de login y registro de usuarios")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso, retorna token JWT",
                content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getContraseña())
            );
            Usuario usuario = usuarioService.buscarPorEmail(request.getEmail());
            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
            return ResponseEntity.ok(new LoginResponse(token, usuario.getRol().name(), usuario.getEmail()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
        }
    }

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con rol CLIENTE")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                content = @Content(schema = @Schema(implementation = listadoDTO.class))),
        @ApiResponse(responseCode = "409", description = "El email ya está registrado", content = @Content)
    })
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody Usuario nuevo) {
        if (usuarioService.existePorEmail(nuevo.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya esta registrado");
        }
        nuevo.setId(null);
        listadoDTO creado = usuarioService.guardarUsuario(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
