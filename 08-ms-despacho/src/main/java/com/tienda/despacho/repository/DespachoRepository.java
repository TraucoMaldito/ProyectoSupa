package com.tienda.despacho.repository;

import com.tienda.despacho.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Integer> {
    List<Despacho> findBySucursalId(Integer sucursalId);
    Optional<Despacho> findByVentaId(Integer ventaId);
    List<Despacho> findByOperadorId(Integer operadorId);
}
