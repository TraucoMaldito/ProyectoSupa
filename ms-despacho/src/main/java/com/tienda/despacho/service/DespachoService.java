package com.tienda.despacho.service;

import com.tienda.despacho.model.Despacho;
import com.tienda.despacho.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DespachoService {

    private final DespachoRepository repository;
    private final WebClient ventaClient;
    private final WebClient sucursalClient;

    public DespachoService(DespachoRepository repository,
                           @Qualifier("ventaClient") WebClient ventaClient,
                           @Qualifier("sucursalClient") WebClient sucursalClient) {
        this.repository = repository;
        this.ventaClient = ventaClient;
        this.sucursalClient = sucursalClient;
    }

    public Despacho crear(Despacho despacho) {
        ventaClient.get()
                .uri("/ventas/" + despacho.getVentaId())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        sucursalClient.get()
                .uri("/sucursales/" + despacho.getSucursalId())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return repository.save(despacho);
    }

    public Despacho buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despacho no encontrado"));
    }

    public List<Despacho> listarPorSucursal(Integer sucursalId) {
        return repository.findBySucursalId(sucursalId);
    }

    public Despacho buscarPorVenta(Integer ventaId) {
        return repository.findByVentaId(ventaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay despacho para esta venta"));
    }

    public Despacho actualizarEstado(Integer id, String nuevoEstado) {
        Despacho despacho = buscarPorId(id);
        despacho.setEstado(nuevoEstado);
        if ("DESPACHADO".equals(nuevoEstado)) {
            despacho.setFechaDespacho(LocalDateTime.now());
        }
        return repository.save(despacho);
    }
}
