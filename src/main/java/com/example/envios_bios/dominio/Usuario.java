package com.example.envios_bios.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @NotBlank
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String nombreUsuario;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String claveDeAcceso;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String email;

    // Getter y Setter
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveDeAcceso() {
        return claveDeAcceso;
    }

    public void setClaveDeAcceso(String claveDeAcceso) {
        this.claveDeAcceso = claveDeAcceso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Constructor por defecto
    public Usuario() {
    }

    // Constructor Completo
    public Usuario(String nombreUsuario, String claveDeAcceso, String email) {
        this.nombreUsuario = nombreUsuario;
        this.claveDeAcceso = claveDeAcceso;
        this.email = email;
    }
}
