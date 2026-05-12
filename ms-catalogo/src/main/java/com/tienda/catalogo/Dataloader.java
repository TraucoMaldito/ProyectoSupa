package com.tienda.catalogo;

import com.tienda.catalogo.model.Categoria;
import com.tienda.catalogo.model.Producto;
import com.tienda.catalogo.repository.CategoriaRepository;
import com.tienda.catalogo.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class Dataloader {

    @Bean
    CommandLineRunner init(CategoriaRepository catRepo, ProductoRepository prodRepo) {
        return args -> {
            if (catRepo.count() == 0) {
                Categoria electronica = catRepo.save(new Categoria(null, "Electronica", "Dispositivos electronicos y accesorios"));
                Categoria ropa = catRepo.save(new Categoria(null, "Ropa", "Prendas de vestir para todas las edades"));
                Categoria hogar = catRepo.save(new Categoria(null, "Hogar", "Articulos para el hogar y decoracion"));

                prodRepo.save(new Producto(null, "Smartphone X200", "Celular de ultima generacion",
                        new BigDecimal("499990"), 50, null, true, electronica));
                prodRepo.save(new Producto(null, "Audifonos Bluetooth", "Audifonos inalambricos con cancelacion de ruido",
                        new BigDecimal("89990"), 100, null, true, electronica));
                prodRepo.save(new Producto(null, "Poleron Deportivo", "Poleron unisex ideal para deporte",
                        new BigDecimal("24990"), 200, null, true, ropa));
                prodRepo.save(new Producto(null, "Lampara LED", "Lampara de escritorio regulable",
                        new BigDecimal("19990"), 75, null, true, hogar));
            }
        };
    }
}
