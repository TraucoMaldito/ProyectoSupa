package com.autenticacion.usuario.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;

    @JsonProperty("contraseña")
    private String password;
}
