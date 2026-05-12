package com.tienda.resena.repository;

import com.tienda.resena.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    List<Resena> findByProductoId(Integer productoId);
    List<Resena> findByClienteId(Integer clienteId);
    boolean existsByClienteIdAndProductoId(Integer clienteId, Integer productoId);
}
