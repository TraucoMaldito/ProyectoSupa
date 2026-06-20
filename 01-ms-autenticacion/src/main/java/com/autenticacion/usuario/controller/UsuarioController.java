package com.autenticacion.usuario.controller;

import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Gestión de usuarios (solo ADMIN)")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar usuarios", description = "Retorna todos los usuarios registrados. Requiere rol ADMIN")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios",
            content = @Content(schema = @Schema(implementation = listadoDTO.class)))
    @GetMapping
    public ResponseEntity<List<listadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Agregar usuario", description = "Crea un nuevo usuario con el rol especificado. Requiere rol ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado",
                content = @Content(schema = @Schema(implementation = listadoDTO.class))),
        @ApiResponse(responseCode = "409", description = "Email ya registrado", content = @Content)
    })
    @PostMapping("/agregar")
    public ResponseEntity<listadoDTO> agregar(@Valid @RequestBody Usuario nuevo) {
        if (service.existePorEmail(nuevo.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarUsuario(nuevo));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID. Requiere rol ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del usuario a eliminar") @PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(u -> {
                    service.eliminarUsuario(id);
                    return ResponseEntity.ok("Usuario eliminado correctamente");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario con id " + id + " no encontrado"));
    }
}
