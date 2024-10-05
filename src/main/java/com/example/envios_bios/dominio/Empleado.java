package com.example.envios_bios.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "empleados")
public class Empleado extends Usuario {

    // Atributos

    @ManyToOne
    private Sucursal sucursal;

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    // Constructor por defecto
    public Empleado() {
        this(null, null, null, null);
    }

    // Constructor completo
    public Empleado(String nombreUsuario, String claveDeAcceso, String email, Sucursal sucursal) {
        super(nombreUsuario, claveDeAcceso, email);
        this.sucursal = sucursal;
    }

}
