package com.tienda.certificacion.repository;

import com.tienda.certificacion.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Integer> {
    Optional<Certificado> findByCodigoVerificacion(String codigo);
    List<Certificado> findByReferenciaId(Integer referenciaId);
    List<Certificado> findByTipoAndReferenciaId(String tipo, Integer referenciaId);
}
