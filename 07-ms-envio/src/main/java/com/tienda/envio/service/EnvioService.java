package com.tienda.envio.service;

import com.tienda.envio.model.Envio;
import com.tienda.envio.repository.EnvioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnvioService {

    private static final Logger log = LoggerFactory.getLogger(EnvioService.class);

    private final EnvioRepository repository;

    public EnvioService(EnvioRepository repository) {
        this.repository = repository;
    }

    public Envio crear(Envio envio) {
        log.info("Creando envio para venta id: {}", envio.getVentaId());
        if (repository.findByVentaId(envio.getVentaId()).isPresent()) {
            log.error("Envio duplicado para venta id: {}", envio.getVentaId());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un envio para esta venta");
        }
        envio.setFechaEstimada(LocalDate.now().plusDays(5));
        Envio guardado = repository.save(envio);
        log.info("Envio creado con id: {}, codigo: {}", guardado.getId(), guardado.getCodigoSeguimiento());
        return guardado;
    }

    public Envio buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Envio no encontrado"));
    }

    public Envio buscarPorVenta(Integer ventaId) {
        return repository.findByVentaId(ventaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay envio para esta venta"));
    }

    public Envio buscarPorCodigo(String codigo) {
        return repository.findByCodigoSeguimiento(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Codigo de seguimiento invalido"));
    }

    public List<Envio> listarPorCliente(Integer clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public Envio actualizarEstado(Integer id, String nuevoEstado) {
        log.info("Actualizando estado de envio id: {} a {}", id, nuevoEstado);
        Envio envio = buscarPorId(id);
        envio.setEstado(nuevoEstado);
        if ("ENTREGADO".equals(nuevoEstado)) {
            envio.setFechaEntrega(LocalDate.now());
            log.info("Envio id: {} marcado como entregado", id);
        }
        return repository.save(envio);
    }
}
