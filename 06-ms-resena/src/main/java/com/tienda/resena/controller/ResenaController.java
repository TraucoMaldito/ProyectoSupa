package com.tienda.resena.controller;

import com.tienda.resena.model.Resena;
import com.tienda.resena.service.ResenaService;
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

@Tag(name = "Reseñas", description = "Gestión de reseñas de productos por clientes")
@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService service;

    public ResenaController(ResenaService service) {
        this.service = service;
    }

    @Operation(summary = "Crear reseña",
            description = "Crea una reseña. El cliente debe haber comprado el producto (estado ENTREGADO). Requiere rol CLIENTE")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reseña creada",
                content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "403", description = "El cliente no ha comprado el producto", content = @Content),
        @ApiResponse(responseCode = "409", description = "Ya existe una reseña de este cliente para este producto", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(resena));
    }

    @Operation(summary = "Listar reseñas por producto")
    @ApiResponse(responseCode = "200", description = "Reseñas del producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Resena>> listarPorProducto(
            @Parameter(description = "ID del producto") @PathVariable Integer productoId) {
        return ResponseEntity.ok(service.listarPorProducto(productoId));
    }

    @Operation(summary = "Listar reseñas por cliente", description = "Requiere rol CLIENTE o ADMIN")
    @ApiResponse(responseCode = "200", description = "Reseñas del cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Resena>> listarPorCliente(
            @Parameter(description = "ID del cliente") @PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @Operation(summary = "Eliminar reseña", description = "Requiere rol ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseña eliminada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la reseña") @PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Resena eliminada correctamente");
    }
}
