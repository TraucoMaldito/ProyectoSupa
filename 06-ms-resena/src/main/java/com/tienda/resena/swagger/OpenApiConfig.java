package com.tienda.resena.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI resenasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Reseñas API")
                        .description("Microservicio de gestión de reseñas de productos. " +
                                "Solo clientes que hayan recibido el producto (estado ENTREGADO) pueden crear reseñas.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo ProyectoSupa")
                                .email("soporte@proyectosupa.cl")
                        )
                );
    }
}
