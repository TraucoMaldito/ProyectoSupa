package com.tienda.sucursales.repository;

import com.tienda.sucursales.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    List<Sucursal> findByActivaTrue();
    List<Sucursal> findByCiudad(String ciudad);
}
