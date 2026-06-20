package com.tienda.despacho.controller;

import com.tienda.despacho.model.Despacho;
import com.tienda.despacho.service.DespachoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despachos")
public class DespachoController {

    private final DespachoService service;

    public DespachoController(DespachoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Despacho> crear(@Valid @RequestBody Despacho despacho) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(despacho));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despacho> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Despacho>> listarPorSucursal(@PathVariable Integer sucursalId) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursalId));
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<Despacho> buscarPorVenta(@PathVariable Integer ventaId) {
        return ResponseEntity.ok(service.buscarPorVenta(ventaId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Despacho> actualizarEstado(@PathVariable Integer id,
                                                     @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(service.actualizarEstado(id, body.get("estado")));
    }
}
