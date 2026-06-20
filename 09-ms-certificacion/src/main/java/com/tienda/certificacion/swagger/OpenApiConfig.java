package com.tienda.certificacion.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI certificacionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Certificación API")
                        .description("Microservicio de emisión y verificación de certificados de despacho. " +
                                "Genera códigos de verificación únicos para validar la autenticidad de los despachos.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipo ProyectoSupa")
                                .email("soporte@proyectosupa.cl")
                        )
                );
    }
}
