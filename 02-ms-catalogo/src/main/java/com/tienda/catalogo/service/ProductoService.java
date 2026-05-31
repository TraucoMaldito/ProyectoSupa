package com.tienda.catalogo.service;

import com.tienda.catalogo.DTO.ProductoDTO;
import com.tienda.catalogo.model.Categoria;
import com.tienda.catalogo.model.Producto;
import com.tienda.catalogo.repository.CategoriaRepository;
import com.tienda.catalogo.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoDTO> listar() {
        log.info("Listando productos activos");
        return productoRepository.findByActivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarPorCategoria(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO buscarPorId(Integer id) {
        return toDTO(findOrThrow(id));
    }

    public ProductoDTO guardar(Producto producto) {
        log.info("Guardando producto: {}", producto.getNombre());
        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Categoria no encontrada"));
        producto.setCategoria(categoria);
        ProductoDTO guardado = toDTO(productoRepository.save(producto));
        log.info("Producto guardado con id: {}", guardado.getId());
        return guardado;
    }

    public ProductoDTO actualizar(Integer id, Producto datos) {
        log.info("Actualizando producto id: {}", id);
        Producto existente = findOrThrow(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        if (datos.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(datos.getCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Categoria no encontrada"));
            existente.setCategoria(categoria);
        }
        return toDTO(productoRepository.save(existente));
    }

    public void eliminar(Integer id) {
        log.info("Desactivando producto id: {}", id);
        Producto producto = findOrThrow(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public void reducirStock(Integer id, Integer cantidad) {
        log.info("Reduciendo stock de producto id: {} en {}", id, cantidad);
        Producto producto = findOrThrow(id);
        if (producto.getStock() < cantidad) {
            log.error("Stock insuficiente para producto id: {}. Stock actual: {}, requerido: {}", id, producto.getStock(), cantidad);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente");
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    private Producto findOrThrow(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado: " + id));
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(),
                p.getActivo(), p.getCategoria().getNombre());
    }
}
