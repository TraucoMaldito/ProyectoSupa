package com.autenticacion.usuario.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autenticacionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Autenticación API")
                        .description("Microservicio de autenticación y gestión de usuarios con JWT. " +
                                "Provee endpoints de login, registro y administración de usuarios.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo ProyectoSupa")
                                .email("soporte@proyectosupa.cl")
                        )
                );
    }
}
