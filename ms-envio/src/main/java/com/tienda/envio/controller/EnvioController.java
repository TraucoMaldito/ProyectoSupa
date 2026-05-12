package com.tienda.envio.controller;

import com.tienda.envio.model.Envio;
import com.tienda.envio.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Envio> crear(@Valid @RequestBody Envio envio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(envio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<Envio> buscarPorVenta(@PathVariable Integer ventaId) {
        return ResponseEntity.ok(service.buscarPorVenta(ventaId));
    }

    @GetMapping("/seguimiento/{codigo}")
    public ResponseEntity<Envio> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Envio>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(@PathVariable Integer id,
                                                  @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}
