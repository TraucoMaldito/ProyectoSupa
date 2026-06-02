package com.tienda.carrito.controller;

import com.tienda.carrito.DTO.AgregarItemRequest;
import com.tienda.carrito.model.Carrito;
import com.tienda.carrito.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Carrito> obtener(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.obtenerCarrito(clienteId));
    }

    @PostMapping("/cliente/{clienteId}/agregar")
    public ResponseEntity<Carrito> agregar(@PathVariable Integer clienteId,
                                           @Valid @RequestBody AgregarItemRequest request) {
        return ResponseEntity.ok(service.agregarItem(clienteId, request));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<String> eliminarItem(@PathVariable Integer itemId) {
        service.eliminarItem(itemId);
        return ResponseEntity.ok("Item eliminado del carrito");
    }

    @DeleteMapping("/cliente/{clienteId}/vaciar")
    public ResponseEntity<String> vaciar(@PathVariable Integer clienteId) {
        service.vaciarCarrito(clienteId);
        return ResponseEntity.ok("Carrito vaciado");
    } 

    @GetMapping("/cliente/{clienteId}/total-items")
      public ResponseEntity<Integer> totalItems(@PathVariable Integer clienteId) {
      return ResponseEntity.ok(service.contarItems(clienteId));
    }
}
