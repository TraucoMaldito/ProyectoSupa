package com.tienda.sucursales.controller;

import com.tienda.sucursales.model.Sucursal;
import com.tienda.sucursales.service.SucursalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Sucursal>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Sucursal> crear(@Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(sucursal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody Sucursal datos) {
        return ResponseEntity.ok(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Sucursal desactivada correctamente");
    }
}
