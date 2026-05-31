package com.autenticacion.usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede quedar vacio")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido no puede quedar vacio")
    @Column(nullable = false, length = 50)
    private String apellido;

    @Email(message = "Por favor usar un email valido")
    @NotBlank(message = "El email no puede quedar vacio")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La direccion no puede quedar vacio")
    @Column(nullable = false, length = 100)
    private String direccion;

    @NotBlank(message = "La contrasena no puede quedar vacia")
    @Column(nullable = false)
    private String contrasena;

    @NotNull(message = "El rol no puede quedar vacio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

}
