package com.autenticacion.usuario.service;

import com.autenticacion.usuario.DTO.listadoDTO;
import com.autenticacion.usuario.model.Rol;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<listadoDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public listadoDTO guardarUsuario(Usuario usuario) {
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return toDTO(repository.save(usuario));
    }

    public void eliminarUsuario(Integer id) {
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
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setDireccion(u.getDireccion());
        return dto;
    }
}
