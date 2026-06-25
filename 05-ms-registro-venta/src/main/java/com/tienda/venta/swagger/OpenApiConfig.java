package com.tienda.venta.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ventasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Registro de Ventas API")
                        .description("Microservicio de registro y gestión de ventas. " +
                                "Crea ventas desde el carrito activo del cliente y administra sus estados.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo ProyectoSupa")
                                .email("soporte@proyectosupa.cl")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
