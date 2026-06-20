package com.tienda.sucursales.controller;

import com.tienda.sucursales.model.Sucursal;
import com.tienda.sucursales.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sucursales", description = "Gestión de sucursales de la tienda")
@RestController
@RequestMapping("/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) { this.service = service; }

    @Operation(summary = "Listar sucursales activas")
    @ApiResponse(responseCode = "200", description = "Lista de sucursales activas")
    @GetMapping
    public ResponseEntity<List<Sucursal>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar sucursal por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada",
                content = @Content(schema = @Schema(implementation = Sucursal.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> buscarPorId(
            @Parameter(description = "ID de la sucursal") @PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear sucursal", description = "Requiere rol ADMIN")
    @ApiResponse(responseCode = "201", description = "Sucursal creada")
    @PostMapping
    public ResponseEntity<Sucursal> crear(@Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(sucursal));
    }

    @Operation(summary = "Actualizar sucursal", description = "Requiere rol ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(
            @Parameter(description = "ID de la sucursal") @PathVariable Integer id,
            @Valid @RequestBody Sucursal datos) {
        return ResponseEntity.ok(service.actualizar(id, datos));
    }

    @Operation(summary = "Desactivar sucursal", description = "Marca la sucursal como inactiva. Requiere rol ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal desactivada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la sucursal") @PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Sucursal desactivada correctamente");
    }
}
