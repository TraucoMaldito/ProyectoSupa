package com.tienda.catalogo.service;

import com.tienda.catalogo.model.Categoria;
import com.tienda.catalogo.repository.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Categoria buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Categoria no encontrada: " + id));
    }

    public Categoria guardar(Categoria categoria) {
        if (repository.existsByNombre(categoria.getNombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una categoria con ese nombre");
        }
        return repository.save(categoria);
    }

    public Categoria actualizar(Integer id, Categoria datos) {
        Categoria existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return repository.save(existente);
    }

    public void eliminar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
