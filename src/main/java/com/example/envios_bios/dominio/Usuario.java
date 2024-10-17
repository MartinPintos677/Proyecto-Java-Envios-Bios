package com.example.envios_bios.dominio;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String claveDeAcceso;

    @Transient
    private String repetirClaveDeAcceso;

    @Email
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String email;

    @ManyToMany
    @JoinTable(joinColumns = { @JoinColumn(name = "usuario_nombre_usuario") }, inverseJoinColumns = {
            @JoinColumn(name = "rol_nombre_rol") })
    private Set<Rol> roles;

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

    public String getRepetirClaveDeAcceso() {
        return repetirClaveDeAcceso;
    }

    public void setRepetirClaveDeAcceso(String repetirClaveDeAcceso) {
        this.repetirClaveDeAcceso = repetirClaveDeAcceso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Rol> getRoles() {
        return this.roles;
    }

    // Constructor por defecto
    public Usuario() {
        this(null, null, null);
    }

    // Constructor Completo
    public Usuario(String nombreUsuario, String claveDeAcceso, String email) {
        this.nombreUsuario = nombreUsuario;
        this.claveDeAcceso = claveDeAcceso;
        this.email = email;

        roles = new HashSet<>();

    }
}
