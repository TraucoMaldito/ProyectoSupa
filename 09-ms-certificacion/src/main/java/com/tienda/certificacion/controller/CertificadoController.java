package com.tienda.certificacion.controller;

import com.tienda.certificacion.model.Certificado;
import com.tienda.certificacion.service.CertificadoService;
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

@Tag(name = "Certificados", description = "Emisión y verificación de certificados de despacho")
@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    private final CertificadoService service;

    public CertificadoController(CertificadoService service) { this.service = service; }

    @Operation(summary = "Emitir certificado",
            description = "Emite un certificado para un despacho. Requiere rol OPERADOR o ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Certificado emitido",
                content = @Content(schema = @Schema(implementation = Certificado.class))),
        @ApiResponse(responseCode = "404", description = "Despacho no encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Certificado> emitir(@Valid @RequestBody Certificado certificado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.emitir(certificado));
    }

    @Operation(summary = "Buscar certificado por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Certificado encontrado"),
        @ApiResponse(responseCode = "404", description = "Certificado no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Certificado> buscarPorId(
            @Parameter(description = "ID del certificado") @PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Verificar certificado por código", description = "Endpoint público para verificar la autenticidad")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Certificado válido"),
        @ApiResponse(responseCode = "404", description = "Código de verificación inválido", content = @Content)
    })
    @GetMapping("/verificar/{codigo}")
    public ResponseEntity<Certificado> verificar(
            @Parameter(description = "Código de verificación del certificado") @PathVariable String codigo) {
        return ResponseEntity.ok(service.verificar(codigo));
    }

    @Operation(summary = "Listar certificados por despacho")
    @ApiResponse(responseCode = "200", description = "Certificados del despacho")
    @GetMapping("/despacho/{despachoId}")
    public ResponseEntity<List<Certificado>> listarPorDespacho(
            @Parameter(description = "ID del despacho") @PathVariable Integer despachoId) {
        return ResponseEntity.ok(service.listarPorDespacho(despachoId));
    }

    @Operation(summary = "Actualizar estado del certificado", description = "Requiere rol ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "404", description = "Certificado no encontrado", content = @Content)
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Certificado> actualizarEstado(
            @Parameter(description = "ID del certificado") @PathVariable Integer id,
            @Parameter(description = "Nuevo estado (ej: REVOCADO, VENCIDO)") @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}
