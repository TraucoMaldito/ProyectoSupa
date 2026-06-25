package com.autenticacion.usuario.DTO;

import lombok.Data;

@Data
public class listadoDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private String rol;
}
