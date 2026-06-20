package com.tienda.carrito.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carritoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Carrito API")
                        .description("Microservicio de gestión del carrito de compras. " +
                                "Permite agregar, eliminar y vaciar items del carrito por cliente.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo ProyectoSupa")
                                .email("soporte@proyectosupa.cl")
                        )
                );
    }
}
