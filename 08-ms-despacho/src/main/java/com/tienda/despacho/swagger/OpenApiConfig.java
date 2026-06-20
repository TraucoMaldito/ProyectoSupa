package com.tienda.despacho.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI despachoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Despacho API")
                        .description("Microservicio de gestión de despachos desde sucursales. " +
                                "Asocia ventas a sucursales y operadores para gestionar el despacho.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo ProyectoSupa")
                                .email("soporte@proyectosupa.cl")
                        )
                );
    }
}
