package com.tienda.resena.controller;

import com.tienda.resena.model.Resena;
import com.tienda.resena.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService service;

    public ResenaController(ResenaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(resena));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Resena>> listarPorProducto(@PathVariable Integer productoId) {
        return ResponseEntity.ok(service.listarPorProducto(productoId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Resena>> listarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Resena eliminada correctamente");
    }
}
