
package com.example.envios_bios.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity

@Table(name = "clientes")
public class Cliente extends Usuario {

    // Atributos

    @NotBlank

    @Size(max = 8, min = 8)

    @Column(nullable = false, length = 8)
    private String cedula;

    @NotBlank

    @Size(max = 200)

    @Column(nullable = false, length = 200)
    private String domicilio;

    @NotBlank

    @Size(max = 20)

    @Column(nullable = false, length = 20)
    private String telefono;

    // Constructor Completo
    public Cliente(String nombreUsuario, String claveDeAcceso, String email,
            String cedula, String domicilio, String telefono) {
        super(nombreUsuario, claveDeAcceso, email);
        this.cedula = cedula;
        this.domicilio = domicilio;
        this.telefono = telefono;
    }

    // Constructor por defecto
    public Cliente() {
        this(null, null, null, null, null, null);
    }

    // Getter y Setter
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
