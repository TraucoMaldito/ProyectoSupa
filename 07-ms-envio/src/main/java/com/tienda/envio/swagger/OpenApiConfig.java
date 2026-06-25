package com.tienda.envio.swagger;

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
    public OpenAPI enviosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Envíos API")
                        .description("Microservicio de gestión de envíos y seguimiento de pedidos. " +
                                "Permite rastrear envíos por código de seguimiento y actualizar estados.")
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
