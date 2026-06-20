package com.autenticacion.usuario.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String contraseña;
}
