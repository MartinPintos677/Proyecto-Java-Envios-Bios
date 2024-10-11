package com.example.envios_bios.dominio;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Column;

@Entity
@Table(name = "paquete")
public class Paquete {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaquete;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nombre_usuario", nullable = false)
    private Cliente cliente;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombreDestinatario;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String telefonoDestinatario;

    @PastOrPresent
    private LocalDateTime fechaHoraRegistro;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String direccionDestinatario;

    private boolean cobroDestinatario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cat", nullable = false)
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rastreo", nullable = false)
    private EstadoRastreo estadoRastreo;

    // Constructor completo
    public Paquete(Long idPaquete, Cliente cliente,
            @NotNull @Size(max = 50) String nombreDestinatario, @NotNull @Size(max = 20) String telefonoDestinatario,
            LocalDateTime fechaHoraRegistro, @NotNull @Size(max = 100) String direccionDestinatario,
            boolean cobroDestinatario, Categoria categoria, EstadoRastreo estadoRastreo) {
        this.idPaquete = idPaquete;
        this.cliente = cliente;
        this.nombreDestinatario = nombreDestinatario;
        this.telefonoDestinatario = telefonoDestinatario;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.direccionDestinatario = direccionDestinatario;
        this.cobroDestinatario = cobroDestinatario;
        this.categoria = categoria;
        this.estadoRastreo = estadoRastreo;
    }

    // Constructor por defecto
    public Paquete() {
        this(null, null, null, null, null, null, false, null, null);
    }

    // Getter y Setter
    public Long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Long idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getTelefonoDestinatario() {
        return telefonoDestinatario;
    }

    public void setTelefonoDestinatario(String telefonoDestinatario) {
        this.telefonoDestinatario = telefonoDestinatario;
    }

    public String getDireccionDestinatario() {
        return direccionDestinatario;
    }

    public void setDireccionDestinatario(String direccionDestinatario) {
        this.direccionDestinatario = direccionDestinatario;
    }

    public boolean isCobroDestinatario() {
        return cobroDestinatario;
    }

    public void setCobroDestinatario(boolean cobroDestinatario) {
        this.cobroDestinatario = cobroDestinatario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstadoRastreo getEstadoRastreo() {
        return estadoRastreo;
    }

    public void setEstadoRastreo(EstadoRastreo estadoRastreo) {
        this.estadoRastreo = estadoRastreo;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }
}