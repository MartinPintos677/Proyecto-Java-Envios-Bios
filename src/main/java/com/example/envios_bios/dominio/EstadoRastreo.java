package com.example.envios_bios.dominio;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "estadoRastreo")
public class EstadoRastreo {

    // Atributos
    @NotNull
    @Id
    private Integer idRastreo;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String descripcion;

    private boolean activo;

    // Constructor completo
    public EstadoRastreo(@NotNull Integer idRastreo, @NotNull @Size(max = 100) String descripcion, boolean activo) {
        this.idRastreo = idRastreo;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    // Constructor por defecto
    public EstadoRastreo() {
        this(null, null, false);
    }

    // Getter y Setter
    public Integer getIdRastreo() {
        return idRastreo;
    }

    public void setIdRastreo(Integer idRastreo) {
        this.idRastreo = idRastreo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
