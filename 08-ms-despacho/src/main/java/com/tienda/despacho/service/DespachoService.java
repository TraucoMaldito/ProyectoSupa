package com.tienda.despacho.service;

import com.tienda.despacho.model.Despacho;
import com.tienda.despacho.repository.DespachoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DespachoService {

    private static final Logger log = LoggerFactory.getLogger(DespachoService.class);

    private final DespachoRepository repository;
    private final RestTemplate restTemplate;

    @Value("${venta.url}")
    private String ventaUrl;

    @Value("${sucursal.url}")
    private String sucursalUrl;

    public DespachoService(DespachoRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Despacho crear(Despacho despacho) {
        log.info("Creando despacho para venta id: {}, sucursal id: {}", despacho.getVentaId(), despacho.getSucursalId());
        restTemplate.getForObject(ventaUrl + "/ventas/" + despacho.getVentaId(), Map.class);
        restTemplate.getForObject(sucursalUrl + "/sucursales/" + despacho.getSucursalId(), Map.class);
        despacho.setEstado("EN_PREPARACION");
        despacho.setFechaCreacion(java.time.LocalDateTime.now());
        Despacho guardado = repository.save(despacho);
        log.info("Despacho creado con id: {}", guardado.getId());
        return guardado;
    }

    public Despacho buscarPorId(Integer id) {
        log.info("Buscando despacho id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despacho no encontrado"));
    }

    public List<Despacho> listarPorSucursal(Integer sucursalId) {
        log.info("Listando despachos de sucursal id: {}", sucursalId);
        return repository.findBySucursalId(sucursalId);
    }

    public Despacho buscarPorVenta(Integer ventaId) {
        log.info("Buscando despacho de venta id: {}", ventaId);
        return repository.findByVentaId(ventaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay despacho para esta venta"));
    }

    public Despacho actualizarEstado(Integer id, String nuevoEstado) {
        log.info("Actualizando estado de despacho id: {} a {}", id, nuevoEstado);
        Despacho despacho = buscarPorId(id);
        despacho.setEstado(nuevoEstado);
        if ("DESPACHADO".equals(nuevoEstado)) {
            despacho.setFechaDespacho(LocalDateTime.now());
            log.info("Despacho id: {} marcado como despachado", id);
        }
        return repository.save(despacho);
    }
}
