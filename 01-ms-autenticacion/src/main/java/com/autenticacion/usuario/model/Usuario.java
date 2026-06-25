package com.autenticacion.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(description = "Entidad de usuario del sistema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Schema(description = "Identificador único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Nombre del usuario", example = "Carlos")
    @NotBlank(message = "El nombre no puede quedar vacio")
    @Column(nullable = false, length = 50)
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Ramírez")
    @NotBlank(message = "El apellido no puede quedar vacio")
    @Column(nullable = false, length = 50)
    private String apellido;

    @Schema(description = "Correo electrónico único del usuario", example = "carlos@tienda.com")
    @Email(message = "Por favor usar un email valido")
    @NotBlank(message = "El email no puede quedar vacio")
    @Column(unique = true)
    private String email;

    @Schema(description = "Dirección de despacho del usuario", example = "Av. Principal 123, Santiago")
    @NotBlank(message = "La direccion no puede quedar vacio")
    @Column(nullable = false, length = 100)
    private String direccion;

    @Schema(description = "Contraseña del usuario (se almacena cifrada)", example = "MiContraseña123!")
    @NotBlank(message = "La contraseña no puede quedar vacía")
    @Column(nullable = false)
    private String contraseña;

    @Schema(description = "Rol del usuario en el sistema", example = "CLIENTE", allowableValues = {"ADMIN", "OPERADOR", "CLIENTE"})
    @NotNull(message = "El rol no puede quedar vacio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

}
