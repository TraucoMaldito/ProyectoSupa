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

                Producto p1 = new Producto();
                p1.setNombre("Smartphone X200");
                p1.setDescripcion("Celular de ultima generacion");
                p1.setPrecio(new BigDecimal("499990"));
                p1.setStock(50);
                p1.setCategoria(electronica);
                prodRepo.save(p1);

                Producto p2 = new Producto();
                p2.setNombre("Audifonos Bluetooth");
                p2.setDescripcion("Audifonos inalambricos con cancelacion de ruido");
                p2.setPrecio(new BigDecimal("89990"));
                p2.setStock(100);
                p2.setCategoria(electronica);
                prodRepo.save(p2);

                Producto p3 = new Producto();
                p3.setNombre("Poleron Deportivo");
                p3.setDescripcion("Poleron unisex ideal para deporte");
                p3.setPrecio(new BigDecimal("24990"));
                p3.setStock(200);
                p3.setCategoria(ropa);
                prodRepo.save(p3);

                Producto p4 = new Producto();
                p4.setNombre("Lampara LED");
                p4.setDescripcion("Lampara de escritorio regulable");
                p4.setPrecio(new BigDecimal("19990"));
                p4.setStock(75);
                p4.setCategoria(hogar);
                prodRepo.save(p4);
            }
        };
    }
}
