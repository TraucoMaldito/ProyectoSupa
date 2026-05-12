package com.tienda.despacho.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${venta.url}")
    private String ventaUrl;

    @Value("${sucursal.url}")
    private String sucursalUrl;

    @Bean
    public WebClient ventaClient() {
        return WebClient.builder().baseUrl(ventaUrl).build();
    }

    @Bean
    public WebClient sucursalClient() {
        return WebClient.builder().baseUrl(sucursalUrl).build();
    }
}
