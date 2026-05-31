package com.tienda.venta.service;

import com.tienda.venta.DTO.ActualizarEstadoRequest;
import com.tienda.venta.DTO.CarritoResponse;
import com.tienda.venta.model.DetalleVenta;
import com.tienda.venta.model.Venta;
import com.tienda.venta.repository.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    private static final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;
    private final RestTemplate restTemplate;

    @Value("${carrito.url}")
    private String carritoUrl;

    @Value("${catalogo.url}")
    private String catalogoUrl;

    public VentaService(VentaRepository ventaRepository, RestTemplate restTemplate) {
        this.ventaRepository = ventaRepository;
        this.restTemplate = restTemplate;
    }

    public Venta crearVenta(Integer clienteId) {
        log.info("Iniciando creacion de venta para cliente id: {}", clienteId);
        CarritoResponse carrito = restTemplate.getForObject(
                carritoUrl + "/carrito/cliente/" + clienteId,
                CarritoResponse.class
        );

        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            log.error("Carrito vacio para cliente id: {}", clienteId);
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

            restTemplate.patchForObject(
                    catalogoUrl + "/productos/" + item.getProductoId() + "/stock?cantidad=" + item.getCantidad(),
                    null,
                    String.class
            );
        }

        venta.setTotal(total);
        Venta guardada = ventaRepository.save(venta);
        log.info("Venta creada con id: {}, total: {}", guardada.getId(), guardada.getTotal());

        restTemplate.delete(carritoUrl + "/carrito/cliente/" + clienteId + "/vaciar");
        log.info("Carrito vaciado para cliente id: {}", clienteId);

        return guardada;
    }

    public List<Venta> listar() {
        log.info("Listando todas las ventas");
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
        log.info("Actualizando estado de venta id: {} a {}", id, request.getEstado());
        Venta venta = buscarPorId(id);
        venta.setEstado(request.getEstado());
        return ventaRepository.save(venta);
    }
}
