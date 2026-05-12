package com.tienda.certificacion.service;

import com.tienda.certificacion.model.Certificado;
import com.tienda.certificacion.repository.CertificadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class CertificadoService {

    private final CertificadoRepository repository;
    private final WebClient despachoClient;

    public CertificadoService(CertificadoRepository repository, WebClient despachoClient) {
        this.repository = repository;
        this.despachoClient = despachoClient;
    }

    public Certificado emitir(Certificado certificado) {
        if ("DESPACHO".equals(certificado.getTipo())) {
            despachoClient.get()
                    .uri("/despachos/" + certificado.getReferenciaId())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        }
        return repository.save(certificado);
    }

    public Certificado buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Certificado no encontrado"));
    }

    public Certificado verificar(String codigo) {
        return repository.findByCodigoVerificacion(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Codigo de verificacion invalido"));
    }

    public List<Certificado> listarPorDespacho(Integer despachoId) {
        return repository.findByTipoAndReferenciaId("DESPACHO", despachoId);
    }

    public Certificado actualizarEstado(Integer id, String nuevoEstado) {
        Certificado cert = buscarPorId(id);
        cert.setEstado(nuevoEstado);
        return repository.save(cert);
    }
}
