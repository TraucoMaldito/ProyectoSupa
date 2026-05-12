package com.tienda.venta.service;

import com.tienda.venta.DTO.ActualizarEstadoRequest;
import com.tienda.venta.DTO.CarritoResponse;
import com.tienda.venta.model.DetalleVenta;
import com.tienda.venta.model.Venta;
import com.tienda.venta.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final WebClient carritoClient;
    private final WebClient catalogoClient;

    public VentaService(VentaRepository ventaRepository,
                        @Qualifier("carritoClient") WebClient carritoClient,
                        @Qualifier("catalogoClient") WebClient catalogoClient) {
        this.ventaRepository = ventaRepository;
        this.carritoClient = carritoClient;
        this.catalogoClient = catalogoClient;
    }

    public Venta crearVenta(Integer clienteId) {
        CarritoResponse carrito = carritoClient.get()
                .uri("/carrito/cliente/" + clienteId)
                .retrieve()
                .bodyToMono(CarritoResponse.class)
                .block();

        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito esta vacio");
        }

        Venta venta = new Venta();
        venta.setClienteId(clienteId);

        BigDecimal total = BigDecimal.ZERO;
        for (CarritoResponse.ItemResponse item : carrito.getItems()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProductoId(item.getProductoId());
            detalle.setNombreProducto(item.getNombreProducto() != null ? item.getNombreProducto() : "Producto");
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            venta.getDetalles().add(detalle);
            total = total.add(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())));

            catalogoClient.patch()
                    .uri("/productos/" + item.getProductoId() + "/stock?cantidad=" + item.getCantidad())
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        }

        venta.setTotal(total);
        Venta guardada = ventaRepository.save(venta);

        carritoClient.delete()
                .uri("/carrito/cliente/" + clienteId + "/vaciar")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();

        return guardada;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada: " + id));
    }

    public List<Venta> buscarPorCliente(Integer clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    public Venta actualizarEstado(Integer id, ActualizarEstadoRequest request) {
        Venta venta = buscarPorId(id);
        venta.setEstado(request.getEstado());
        return ventaRepository.save(venta);
    }
}
