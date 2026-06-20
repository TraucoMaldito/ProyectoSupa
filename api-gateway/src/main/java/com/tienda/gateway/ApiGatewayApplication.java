package com.tienda.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("================================================");
        System.out.println(" API Gateway iniciado correctamente");
        System.out.println(" URL: http://localhost:8090");
        System.out.println("------------------------------------------------");
        System.out.println(" /auth/**         -> MS-AUTENTICACION");
        System.out.println(" /usuarios/**     -> MS-AUTENTICACION");
        System.out.println(" /categorias/**   -> MS-CATALOGO");
        System.out.println(" /productos/**    -> MS-CATALOGO");
        System.out.println(" /sucursales/**   -> MS-SUCURSALES");
        System.out.println(" /carrito/**      -> MS-CARRITO");
        System.out.println(" /ventas/**       -> MS-REGISTRO-VENTA");
        System.out.println(" /resenas/**      -> MS-RESENA");
        System.out.println(" /envios/**       -> MS-ENVIO");
        System.out.println(" /despachos/**    -> MS-DESPACHO");
        System.out.println(" /certificados/** -> MS-CERTIFICACION");
        System.out.println("------------------------------------------------");
        System.out.println(" Eureka: http://localhost:8761");
        System.out.println("================================================");
    }
}
