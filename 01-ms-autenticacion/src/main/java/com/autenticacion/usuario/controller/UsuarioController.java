package com.autenticacion.usuario.controller;

import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<listadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/agregar")
    public ResponseEntity<listadoDTO> agregar(@Valid @RequestBody Usuario nuevo) {
        if (service.existePorEmail(nuevo.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarUsuario(nuevo));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(u -> {
                    service.eliminarUsuario(id);
                    return ResponseEntity.ok("Usuario eliminado correctamente");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario con id " + id + " no encontrado"));
    }
}
