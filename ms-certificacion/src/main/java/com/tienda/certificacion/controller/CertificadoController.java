package com.tienda.certificacion.controller;

import com.tienda.certificacion.model.Certificado;
import com.tienda.certificacion.service.CertificadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    private final CertificadoService service;

    public CertificadoController(CertificadoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Certificado> emitir(@Valid @RequestBody Certificado certificado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.emitir(certificado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificado> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/verificar/{codigo}")
    public ResponseEntity<Certificado> verificar(@PathVariable String codigo) {
        return ResponseEntity.ok(service.verificar(codigo));
    }

    @GetMapping("/despacho/{despachoId}")
    public ResponseEntity<List<Certificado>> listarPorDespacho(@PathVariable Integer despachoId) {
        return ResponseEntity.ok(service.listarPorDespacho(despachoId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Certificado> actualizarEstado(@PathVariable Integer id,
                                                        @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}
