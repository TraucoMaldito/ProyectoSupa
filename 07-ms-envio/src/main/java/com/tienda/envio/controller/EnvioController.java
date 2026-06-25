package com.tienda.envio.controller;

import com.tienda.envio.model.Envio;
import com.tienda.envio.service.EnvioService;
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

@Tag(name = "Envíos", description = "Gestión de envíos y seguimiento de pedidos")
@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) { this.service = service; }

    @Operation(summary = "Crear envío", description = "Registra un envío para una venta. Requiere rol OPERADOR o ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Envío creado",
                content = @Content(schema = @Schema(implementation = Envio.class))),
        @ApiResponse(responseCode = "409", description = "Ya existe un envío para esta venta", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Envio> crear(@Valid @RequestBody Envio envio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(envio));
    }

    @Operation(summary = "Buscar envío por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(
            @Parameter(description = "ID del envío") @PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar envío por venta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "No hay envío para esta venta", content = @Content)
    })
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<Envio> buscarPorVenta(
            @Parameter(description = "ID de la venta") @PathVariable Integer ventaId) {
        return ResponseEntity.ok(service.buscarPorVenta(ventaId));
    }

    @Operation(summary = "Rastrear envío por código de seguimiento", description = "Endpoint público")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Código de seguimiento inválido", content = @Content)
    })
    @GetMapping("/seguimiento/{codigo}")
    public ResponseEntity<Envio> buscarPorCodigo(
            @Parameter(description = "Código de seguimiento") @PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    @Operation(summary = "Listar envíos por cliente")
    @ApiResponse(responseCode = "200", description = "Envíos del cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Envio>> listarPorCliente(
            @Parameter(description = "ID del cliente") @PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @Operation(summary = "Actualizar estado del envío", description = "Requiere rol OPERADOR o ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado", content = @Content)
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(
            @Parameter(description = "ID del envío") @PathVariable Integer id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}
