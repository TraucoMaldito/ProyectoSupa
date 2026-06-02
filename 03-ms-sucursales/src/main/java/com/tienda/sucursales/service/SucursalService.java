package com.tienda.sucursales.service;

import com.tienda.sucursales.model.Sucursal;
import com.tienda.sucursales.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SucursalService {

    private static final Logger log = LoggerFactory.getLogger(SucursalService.class);

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> listar() {
        log.info("Listando sucursales activas");
        return repository.findByActivaTrue();
    }

    public List<Sucursal> listarTodas() {
        log.info("Listando todas las sucursales");
        return repository.findAll();
    }

    public Sucursal buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sucursal no encontrada: " + id));
    }

    public Sucursal guardar(Sucursal sucursal) {
        log.info("Guardando sucursal: {}", sucursal.getNombre());
        sucursal.setActiva(true);
        Sucursal guardada = repository.save(sucursal);
        log.info("Sucursal guardada con id: {}", guardada.getId());
        return guardada;
    }

    public Sucursal actualizar(Integer id, Sucursal datos) {
        log.info("Actualizando sucursal id: {}", id);
        Sucursal existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDireccion(datos.getDireccion());
        existente.setCiudad(datos.getCiudad());
        existente.setTelefono(datos.getTelefono());
        existente.setHorarioApertura(datos.getHorarioApertura());
        existente.setHorarioCierre(datos.getHorarioCierre());
        return repository.save(existente);
    }

    public void eliminar(Integer id) {
        log.info("Desactivando sucursal id: {}", id);
        Sucursal sucursal = buscarPorId(id);
        sucursal.setActiva(false);
        repository.save(sucursal);
    }
}
