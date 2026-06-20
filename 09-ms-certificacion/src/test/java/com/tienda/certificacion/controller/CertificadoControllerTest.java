package com.tienda.certificacion.controller;

import com.tienda.certificacion.model.Certificado;
import com.tienda.certificacion.security.JwtUtil;
import com.tienda.certificacion.service.CertificadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CertificadoController.class)
@WithMockUser(roles = "ADMIN")
@DisplayName("Controlador de Certificados")
public class CertificadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CertificadoService certificadoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Certificado certificadoBase() {
        Certificado cert = new Certificado();
        cert.setId(1);
        cert.setTipo("DESPACHO");
        cert.setReferenciaId(50);
        cert.setOperadorId(7);
        cert.setDescripcion("Certificado de despacho #50");
        cert.setEstado("VIGENTE");
        cert.setCodigoVerificacion("CERT-ABC-12345");
        return cert;
    }

    @Test
    @DisplayName("Emitir - certificado válido retorna 201")
    void emitir_certificadoValido_retorna201() throws Exception {
        Certificado nuevo = new Certificado();
        nuevo.setTipo("DESPACHO");
        nuevo.setReferenciaId(50);
        nuevo.setOperadorId(7);

        when(certificadoService.emitir(any(Certificado.class))).thenReturn(certificadoBase());

        mockMvc.perform(post("/certificados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("DESPACHO"))
                .andExpect(jsonPath("$.codigoVerificacion").value("CERT-ABC-12345"));
    }

    @Test
    @DisplayName("Emitir - despacho inexistente retorna 404")
    void emitir_despachoInexistente_retorna404() throws Exception {
        Certificado nuevo = new Certificado();
        nuevo.setTipo("DESPACHO");
        nuevo.setReferenciaId(999);
        nuevo.setOperadorId(7);

        when(certificadoService.emitir(any(Certificado.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Despacho no encontrado"));

        mockMvc.perform(post("/certificados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Buscar por ID - certificado existente retorna 200")
    void buscarPorId_certificadoExistente_retorna200() throws Exception {
        when(certificadoService.buscarPorId(1)).thenReturn(certificadoBase());

        mockMvc.perform(get("/certificados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("VIGENTE"));
    }

    @Test
    @DisplayName("Buscar por ID - certificado inexistente retorna 404")
    void buscarPorId_certificadoInexistente_retorna404() throws Exception {
        when(certificadoService.buscarPorId(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));

        mockMvc.perform(get("/certificados/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verificar - código válido retorna certificado")
    void verificar_codigoValido_retornaCertificado() throws Exception {
        when(certificadoService.verificar("CERT-ABC-12345")).thenReturn(certificadoBase());

        mockMvc.perform(get("/certificados/verificar/CERT-ABC-12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoVerificacion").value("CERT-ABC-12345"))
                .andExpect(jsonPath("$.estado").value("VIGENTE"));
    }

    @Test
    @DisplayName("Verificar - código inválido retorna 404")
    void verificar_codigoInvalido_retorna404() throws Exception {
        when(certificadoService.verificar("INVALIDO"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Código inválido"));

        mockMvc.perform(get("/certificados/verificar/INVALIDO"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Listar por despacho - retorna certificados")
    void listarPorDespacho_retornaCertificados() throws Exception {
        when(certificadoService.listarPorDespacho(50)).thenReturn(List.of(certificadoBase()));

        mockMvc.perform(get("/certificados/despacho/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("DESPACHO"));
    }

    @Test
    @DisplayName("Actualizar estado - retorna 200")
    void actualizarEstado_retorna200() throws Exception {
        Certificado actualizado = certificadoBase();
        actualizado.setEstado("REVOCADO");

        when(certificadoService.actualizarEstado(1, "REVOCADO")).thenReturn(actualizado);

        mockMvc.perform(put("/certificados/1/estado")
                        .param("estado", "REVOCADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("REVOCADO"));
    }
}
