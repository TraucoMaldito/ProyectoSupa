package com.autenticacion.usuario.service;

import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Rol;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<listadoDTO> listar() {
        log.info("Listando todos los usuarios");
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public listadoDTO guardarUsuario(Usuario usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getEmail());
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        listadoDTO creado = toDTO(repository.save(usuario));
        log.info("Usuario registrado exitosamente: {}", usuario.getEmail());
        return creado;
    }

    public void eliminarUsuario(Integer id) {
        log.info("Eliminando usuario id: {}", id);
        repository.deleteById(id);
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }

    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    private listadoDTO toDTO(Usuario u) {
        listadoDTO dto = new listadoDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setDireccion(u.getDireccion());
        dto.setRol(u.getRol() != null ? u.getRol().name() : null);
        return dto;
    }
}
