package com.example.envios_bios.dominio;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "categorias")
public class Categoria {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCat;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    // Contructor Completo
    public Categoria(Integer idCat, String nombre) {
        this.idCat = idCat;
        this.nombre = nombre;
    }

    // Constructor por Defecto
    public Categoria() {
        this(null, null);
    }

    // Getter y Setter
    public Integer getIdCat() {
        return idCat;
    }

    public void setIdCat(Integer idCat) {
        this.idCat = idCat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
