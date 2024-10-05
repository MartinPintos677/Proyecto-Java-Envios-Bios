package com.example.envios_bios.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Sucursal")
public class Sucursal {

    // Atributos
    @NotNull
    @Min(1)
    @Id
    private Long numero;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombre;

    // Constructor completo
    public Sucursal(@NotNull @Min(1) Long numero, @NotNull String nombre) {
        this.numero = numero;
        this.nombre = nombre;
    }

    // Constructor por defecto
    public Sucursal() {
        this(null, null);
    }

    // Getter y Setter
    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
