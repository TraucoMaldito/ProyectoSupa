package com.tienda.carrito.repository;

import com.tienda.carrito.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByClienteIdAndEstado(Integer clienteId, String estado);
}
