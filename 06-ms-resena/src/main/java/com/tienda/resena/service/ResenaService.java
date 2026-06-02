package com.tienda.resena.service;

import com.tienda.resena.model.Resena;
import com.tienda.resena.repository.ResenaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class ResenaService {

    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);

    private final ResenaRepository repository;
    private final RestTemplate restTemplate;

    @Value("${venta.url}")
    private String ventaUrl;

    public ResenaService(ResenaRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Resena crear(Resena resena) {
        log.info("Creando resena - cliente id: {}, producto id: {}", resena.getClienteId(), resena.getProductoId());
        if (repository.existsByClienteIdAndProductoId(resena.getClienteId(), resena.getProductoId())) {
            log.error("Resena duplicada - cliente id: {}, producto id: {}", resena.getClienteId(), resena.getProductoId());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una resena para este producto por este cliente");
        }

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                ventaUrl + "/ventas/cliente/" + resena.getClienteId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> ventas = response.getBody();
        boolean haComprado = ventas != null && ventas.stream()
                .anyMatch(v -> "ENTREGADO".equals(v.get("estado")));

        if (!haComprado) {
            log.error("Cliente id: {} intento resenar producto id: {} sin haberlo comprado", resena.getClienteId(), resena.getProductoId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo puedes resenir productos que hayas comprado");
        }

        resena.setFecha(java.time.LocalDateTime.now());
        Resena guardada = repository.save(resena);
        log.info("Resena creada con id: {}", guardada.getId());
        return guardada;
    }

    public List<Resena> listarPorProducto(Integer productoId) {
        log.info("Listando resenas del producto id: {}", productoId);
        return repository.findByProductoId(productoId);
    }

    public List<Resena> listarPorCliente(Integer clienteId) {
        log.info("Listando resenas del cliente id: {}", clienteId);
        return repository.findByClienteId(clienteId);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando resena id: {}", id);
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resena no encontrada"));
        repository.deleteById(id);
    }
}
