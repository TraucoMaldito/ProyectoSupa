package com.tienda.venta.controller;

import com.tienda.venta.DTO.ActualizarEstadoRequest;
import com.tienda.venta.DTO.ComprarRequest;
import com.tienda.venta.model.Venta;
import com.tienda.venta.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Venta> comprar(@Valid @RequestBody ComprarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearVenta(request.getClienteId()));
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> buscarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.buscarPorCliente(clienteId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Venta> actualizarEstado(@PathVariable Integer id,
                                                  @Valid @RequestBody ActualizarEstadoRequest request) {
        return ResponseEntity.ok(service.actualizarEstado(id, request));
    }
}
