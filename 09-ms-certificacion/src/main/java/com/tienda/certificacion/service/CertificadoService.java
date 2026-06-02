package com.tienda.certificacion.service;

import com.tienda.certificacion.model.Certificado;
import com.tienda.certificacion.repository.CertificadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class CertificadoService {

    private static final Logger log = LoggerFactory.getLogger(CertificadoService.class);

    private final CertificadoRepository repository;
    private final RestTemplate restTemplate;

    @Value("${despacho.url}")
    private String despachoUrl;

    public CertificadoService(CertificadoRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Certificado emitir(Certificado certificado) {
        log.info("Emitiendo certificado tipo: {}, referencia id: {}", certificado.getTipo(), certificado.getReferenciaId());
        if ("DESPACHO".equals(certificado.getTipo())) {
            restTemplate.getForObject(despachoUrl + "/despachos/" + certificado.getReferenciaId(), Map.class);
        }
        certificado.setEstado("VIGENTE");
        certificado.setFechaEmision(java.time.LocalDateTime.now());
        certificado.setCodigoVerificacion(java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        Certificado guardado = repository.save(certificado);
        log.info("Certificado emitido con id: {}, codigo: {}", guardado.getId(), guardado.getCodigoVerificacion());
        return guardado;
    }

    public Certificado buscarPorId(Integer id) {
        log.info("Buscando certificado id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Certificado no encontrado"));
    }

    public Certificado verificar(String codigo) {
        log.info("Verificando certificado con codigo: {}", codigo);
        return repository.findByCodigoVerificacion(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Codigo de verificacion invalido"));
    }

    public List<Certificado> listarPorDespacho(Integer despachoId) {
        log.info("Listando certificados del despacho id: {}", despachoId);
        return repository.findByTipoAndReferenciaId("DESPACHO", despachoId);
    }

    public Certificado actualizarEstado(Integer id, String nuevoEstado) {
        log.info("Actualizando estado de certificado id: {} a {}", id, nuevoEstado);
        Certificado cert = buscarPorId(id);
        cert.setEstado(nuevoEstado);
        return repository.save(cert);
    }
}
