package com.tienda.carrito.repository;

import com.tienda.carrito.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Integer> {
    Optional<ItemCarrito> findByCarritoIdAndProductoId(Integer carritoId, Integer productoId);
}
