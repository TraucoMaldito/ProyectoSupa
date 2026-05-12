package com.tienda.venta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${carrito.url}")
    private String carritoUrl;

    @Value("${catalogo.url}")
    private String catalogoUrl;

    @Bean
    public WebClient carritoClient() {
        return WebClient.builder().baseUrl(carritoUrl).build();
    }

    @Bean
    public WebClient catalogoClient() {
        return WebClient.builder().baseUrl(catalogoUrl).build();
    }
}
