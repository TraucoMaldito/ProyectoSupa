package com.tienda.carrito.service;

import com.tienda.carrito.DTO.AgregarItemRequest;
import com.tienda.carrito.DTO.ProductoResponse;
import com.tienda.carrito.model.Carrito;
import com.tienda.carrito.model.ItemCarrito;
import com.tienda.carrito.repository.CarritoRepository;
import com.tienda.carrito.repository.ItemCarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemRepository;
    private final RestTemplate restTemplate;

    @Value("${catalogo.url}")
    private String catalogoUrl;

    public CarritoService(CarritoRepository carritoRepository,
                          ItemCarritoRepository itemRepository,
                          RestTemplate restTemplate) {
        this.carritoRepository = carritoRepository;
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
    }

    public Carrito obtenerCarrito(Integer clienteId) {
        log.info("Obteniendo carrito activo del cliente id: {}", clienteId);
        return carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setClienteId(clienteId);
                    return carritoRepository.save(nuevo);
                });
    }

    public Carrito agregarItem(Integer clienteId, AgregarItemRequest request) {
        log.info("Agregando item - cliente id: {}, producto id: {}, cantidad: {}", clienteId, request.getProductoId(), request.getCantidad());
        ProductoResponse producto = restTemplate.getForObject(
                catalogoUrl + "/productos/" + request.getProductoId(),
                ProductoResponse.class
        );

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
                            ItemCarrito nuevo = new ItemCarrito();
                            nuevo.setCarrito(carrito);
                            nuevo.setProductoId(request.getProductoId());
                            nuevo.setNombreProducto(producto.getNombre());
                            nuevo.setCantidad(request.getCantidad());
                            nuevo.setPrecioUnitario(producto.getPrecio());
                            itemRepository.save(nuevo);
                            carrito.getItems().add(nuevo);
                        }
                );

        return carrito;
    }

    public void eliminarItem(Integer itemId) {
        log.info("Eliminando item id: {}", itemId);
        ItemCarrito item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado"));
        itemRepository.delete(item);
    }

    public void vaciarCarrito(Integer clienteId) {
        log.info("Vaciando carrito del cliente id: {}", clienteId);
        carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .ifPresent(c -> {
                    c.getItems().clear();
                    carritoRepository.save(c);
                });
    }

    public void cerrarCarrito(Integer clienteId) {
        log.info("Cerrando carrito del cliente id: {}", clienteId);
        carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .ifPresent(c -> {
                    c.setEstado("CERRADO");
                    carritoRepository.save(c);
                });
    }
    public int contarItems(Integer clienteId) {
    Carrito carrito = obtenerCarrito(clienteId);
    return carrito.getItems().size();
    }

}
