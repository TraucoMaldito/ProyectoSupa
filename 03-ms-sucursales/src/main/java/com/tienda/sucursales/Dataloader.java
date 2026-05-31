package com.tienda.sucursales;

import com.tienda.sucursales.model.Sucursal;
import com.tienda.sucursales.repository.SucursalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Dataloader {

    @Bean
    CommandLineRunner init(SucursalRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Sucursal(null, "Sucursal Centro", "Av. Principal 100", "Santiago", "+56912345678", "09:00", "20:00", true));
                repository.save(new Sucursal(null, "Sucursal Norte", "Calle Norte 250", "Antofagasta", "+56987654321", "09:00", "19:00", true));
                repository.save(new Sucursal(null, "Sucursal Sur", "Pasaje Sur 45", "Temuco", "+56911223344", "10:00", "18:00", true));
            }
        };
    }
}
