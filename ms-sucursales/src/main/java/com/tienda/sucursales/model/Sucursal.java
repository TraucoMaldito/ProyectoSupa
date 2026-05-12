package com.tienda.sucursales.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sucursales")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede quedar vacio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La direccion no puede quedar vacia")
    @Column(nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "La ciudad no puede quedar vacia")
    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(length = 20)
    private String telefono;

    @Column(name = "horario_apertura", length = 10)
    private String horarioApertura;

    @Column(name = "horario_cierre", length = 10)
    private String horarioCierre;

    @Column(nullable = false)
    private Boolean activa = true;
}
