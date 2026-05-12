package com.tienda.resena.service;

import com.tienda.resena.model.Resena;
import com.tienda.resena.repository.ResenaRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class ResenaService {

    private final ResenaRepository repository;
    private final WebClient ventaClient;

    public ResenaService(ResenaRepository repository, WebClient ventaClient) {
        this.repository = repository;
        this.ventaClient = ventaClient;
    }

    public Resena crear(Resena resena) {
        if (repository.existsByClienteIdAndProductoId(resena.getClienteId(), resena.getProductoId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una resena para este producto por este cliente");
        }

        List<Map<String, Object>> ventas = ventaClient.get()
                .uri("/ventas/cliente/" + resena.getClienteId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        boolean haComprado = ventas != null && ventas.stream()
                .anyMatch(v -> "ENTREGADO".equals(v.get("estado")));

        if (!haComprado) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo puedes resenir productos que hayas comprado");
        }

        return repository.save(resena);
    }

    public List<Resena> listarPorProducto(Integer productoId) {
        return repository.findByProductoId(productoId);
    }

    public List<Resena> listarPorCliente(Integer clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public void eliminar(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resena no encontrada"));
        repository.deleteById(id);
    }
}
