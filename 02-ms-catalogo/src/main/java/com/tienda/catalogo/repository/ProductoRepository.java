package com.tienda.catalogo.repository;

import com.tienda.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoriaId(Integer categoriaId);
    List<Producto> findByActivoTrue();
    boolean existsByCategoriaId(Integer categoriaId);
}
