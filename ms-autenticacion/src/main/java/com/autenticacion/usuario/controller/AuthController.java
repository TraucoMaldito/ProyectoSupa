package com.autenticacion.usuario.controller;

import com.autenticacion.usuario.DTO.LoginRequest;
import com.autenticacion.usuario.DTO.LoginResponse;
import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Rol;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.security.JwtUtil;
import com.autenticacion.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            Usuario usuario = usuarioService.buscarPorEmail(request.getEmail());
            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
            return ResponseEntity.ok(new LoginResponse(token, usuario.getRol().name(), usuario.getEmail()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody Usuario nuevo) {
        if (usuarioService.existePorEmail(nuevo.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya esta registrado");
        }
        nuevo.setRol(Rol.CLIENTE);
        listadoDTO creado = usuarioService.guardarUsuario(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
