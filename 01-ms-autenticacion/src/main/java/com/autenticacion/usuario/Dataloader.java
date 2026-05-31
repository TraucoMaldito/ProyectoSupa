package com.autenticacion.usuario;

import com.autenticacion.usuario.model.Rol;
import com.autenticacion.usuario.model.Usuario;
import com.autenticacion.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Dataloader {

    @Bean
    CommandLineRunner init(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Usuario(null,
                        "Admin", "Sistema",
                        "admin@tienda.com", "Oficina Central",
                        passwordEncoder.encode("Admin1234!"), Rol.ADMIN));

                repository.save(new Usuario(null,
                        "Carlos", "Ramirez",
                        "operador@tienda.com", "Sucursal Norte",
                        passwordEncoder.encode("Operador1234!"), Rol.OPERADOR));

                repository.save(new Usuario(null,
                        "Maria", "Lopez",
                        "cliente@tienda.com", "Av. Principal 123",
                        passwordEncoder.encode("Cliente1234!"), Rol.CLIENTE));
            }
        };
    }
}
