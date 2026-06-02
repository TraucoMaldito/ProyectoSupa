package com.tienda.catalogo.controller;

import com.tienda.catalogo.DTO.ProductoDTO;
import com.tienda.catalogo.model.Producto;
import com.tienda.catalogo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(service.listarPorCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id,
                                                  @Valid @RequestBody Producto datos) {
        return ResponseEntity.ok(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Producto desactivado correctamente");
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<String> reducirStock(@PathVariable Integer id,
                                               @RequestParam Integer cantidad) {
        service.reducirStock(id, cantidad);
        return ResponseEntity.ok("Stock actualizado");
    }
}
