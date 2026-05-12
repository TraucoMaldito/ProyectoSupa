package com.tienda.envio.repository;

import com.tienda.envio.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Optional<Envio> findByVentaId(Integer ventaId);
    Optional<Envio> findByCodigoSeguimiento(String codigo);
    List<Envio> findByClienteId(Integer clienteId);
}
