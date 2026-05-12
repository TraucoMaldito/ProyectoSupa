package com.tienda.envio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${venta.url}")
    private String ventaUrl;

    @Bean
    public WebClient ventaClient() {
        return WebClient.builder().baseUrl(ventaUrl).build();
    }
}
