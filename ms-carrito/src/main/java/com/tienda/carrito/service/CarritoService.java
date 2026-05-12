package com.tienda.carrito.service;

import com.tienda.carrito.DTO.AgregarItemRequest;
import com.tienda.carrito.DTO.ProductoResponse;
import com.tienda.carrito.model.Carrito;
import com.tienda.carrito.model.ItemCarrito;
import com.tienda.carrito.repository.CarritoRepository;
import com.tienda.carrito.repository.ItemCarritoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemRepository;
    private final WebClient catalogoClient;

    public CarritoService(CarritoRepository carritoRepository,
                          ItemCarritoRepository itemRepository,
                          WebClient catalogoClient) {
        this.carritoRepository = carritoRepository;
        this.itemRepository = itemRepository;
        this.catalogoClient = catalogoClient;
    }

    public Carrito obtenerCarrito(Integer clienteId) {
        return carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .orElseGet(() -> carritoRepository.save(new Carrito(null, clienteId, "ACTIVO", null, null)));
    }

    public Carrito agregarItem(Integer clienteId, AgregarItemRequest request) {
        ProductoResponse producto = catalogoClient.get()
                .uri("/productos/" + request.getProductoId())
                .retrieve()
                .bodyToMono(ProductoResponse.class)
                .block();

        if (producto == null || !Boolean.TRUE.equals(producto.getActivo())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no disponible");
        }
        if (producto.getStock() < request.getCantidad()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente");
        }

        Carrito carrito = obtenerCarrito(clienteId);

        itemRepository.findByCarritoIdAndProductoId(carrito.getId(), request.getProductoId())
                .ifPresentOrElse(
                        item -> {
                            item.setCantidad(item.getCantidad() + request.getCantidad());
                            itemRepository.save(item);
                        },
                        () -> {
                            ItemCarrito nuevo = new ItemCarrito(null, carrito,
                                    request.getProductoId(), request.getCantidad(), producto.getPrecio());
                            itemRepository.save(nuevo);
                        }
                );

        return carritoRepository.findById(carrito.getId()).orElseThrow();
    }

    public void eliminarItem(Integer itemId) {
        ItemCarrito item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado"));
        itemRepository.delete(item);
    }

    public void vaciarCarrito(Integer clienteId) {
        carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .ifPresent(c -> {
                    c.getItems().clear();
                    carritoRepository.save(c);
                });
    }

    public void cerrarCarrito(Integer clienteId) {
        carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .ifPresent(c -> {
                    c.setEstado("CERRADO");
                    carritoRepository.save(c);
                });
    }
}
