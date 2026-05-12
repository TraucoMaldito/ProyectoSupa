package com.tienda.sucursales.service;

import com.tienda.sucursales.model.Sucursal;
import com.tienda.sucursales.repository.SucursalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> listar() {
        return repository.findByActivaTrue();
    }

    public List<Sucursal> listarTodas() {
        return repository.findAll();
    }

    public Sucursal buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sucursal no encontrada: " + id));
    }

    public Sucursal guardar(Sucursal sucursal) {
        return repository.save(sucursal);
    }

    public Sucursal actualizar(Integer id, Sucursal datos) {
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
        Sucursal sucursal = buscarPorId(id);
        sucursal.setActiva(false);
        repository.save(sucursal);
    }
}
