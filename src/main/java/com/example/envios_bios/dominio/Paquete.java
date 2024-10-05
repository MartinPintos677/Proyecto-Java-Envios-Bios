/*
 * package com.example.envios_bios.dominio;
 * 
 * import java.time.LocalDateTime;
 * 
 * import jakarta.persistence.Entity;
 * import jakarta.persistence.Id;
 * import jakarta.persistence.ManyToOne;
 * import jakarta.persistence.Table;
 * import jakarta.validation.constraints.Min;
 * import jakarta.validation.constraints.NotBlank;
 * import jakarta.validation.constraints.NotNull;
 * import jakarta.validation.constraints.PastOrPresent;
 * import jakarta.validation.constraints.Size;
 * import jakarta.persistence.Column;
 * 
 * @Entity
 * 
 * @Table(name = "Paquete")
 * public class Paquete {
 * 
 * // Atributos
 * 
 * @NotNull
 * 
 * @Min(1)
 * 
 * @Id
 * private Integer idPaquete;
 * 
 * @NotBlank
 * 
 * @Size(max = 50)
 * 
 * @Column(nullable = false, length = 50)
 * private Cliente nombreUsuario;
 * 
 * @NotBlank
 * 
 * @Size(max = 50)
 * 
 * @Column(nullable = false, length = 50)
 * private String nombreDestinatario;
 * 
 * @NotBlank
 * 
 * @Size(max = 20)
 * 
 * @Column(nullable = false, length = 20)
 * private String telefonoDestinatario;
 * 
 * @PastOrPresent
 * private LocalDateTime fechaHoraRegistro;
 * 
 * @NotBlank
 * 
 * @Size(max = 100)
 * 
 * @Column(nullable = false, length = 100)
 * private String direccionDestinatario;
 * 
 * private boolean cobroDestinatario;
 * 
 * @ManyToOne
 * private Categoria categoria;
 * 
 * @ManyToOne
 * private EstadoRastreo estadoRastreo;
 * 
 * // Constructor completo
 * public Paquete(@NotNull @Min(1) Integer idPaquete, @NotNull @Size(max = 50)
 * Cliente nombreUsuario,
 * 
 * @NotNull @Size(max = 50) String nombreDestinatario, @NotNull @Size(max = 20)
 * String telefonoDestinatario,
 * LocalDateTime fechaHoraRegistro, @NotNull @Size(max = 100) String
 * direccionDestinatario,
 * boolean cobroDestinatario, @NotNull Categoria categoria, @NotNull
 * EstadoRastreo estadoRastreo) {
 * this.idPaquete = idPaquete;
 * this.nombreUsuario = nombreUsuario;
 * this.nombreDestinatario = nombreDestinatario;
 * this.telefonoDestinatario = telefonoDestinatario;
 * this.fechaHoraRegistro = fechaHoraRegistro;
 * this.direccionDestinatario = direccionDestinatario;
 * this.cobroDestinatario = cobroDestinatario;
 * this.categoria = categoria;
 * this.estadoRastreo = estadoRastreo;
 * }
 * 
 * // Constructor por defecto
 * public Paquete() {
 * this(null, null, null, null, null, null, false, null, null);
 * }
 * 
 * // Getter y Setter
 * public Integer getIdPaquete() {
 * return idPaquete;
 * }
 * 
 * public void setIdPaquete(Integer idPaquete) {
 * this.idPaquete = idPaquete;
 * }
 * 
 * public Cliente getNombreUsuario() {
 * return nombreUsuario;
 * }
 * 
 * public void setNombreUsuario(Cliente nombreUsuario) {
 * this.nombreUsuario = nombreUsuario;
 * }
 * 
 * public String getNombreDestinatario() {
 * return nombreDestinatario;
 * }
 * 
 * public void setNombreDestinatario(String nombreDestinatario) {
 * this.nombreDestinatario = nombreDestinatario;
 * }
 * 
 * public String getTelefonoDestinatario() {
 * return telefonoDestinatario;
 * }
 * 
 * public void setTelefonoDestinatario(String telefonoDestinatario) {
 * this.telefonoDestinatario = telefonoDestinatario;
 * }
 * 
 * public String getDireccionDestinatario() {
 * return direccionDestinatario;
 * }
 * 
 * public void setDireccionDestinatario(String direccionDestinatario) {
 * this.direccionDestinatario = direccionDestinatario;
 * }
 * 
 * public boolean isCobroDestinatario() {
 * return cobroDestinatario;
 * }
 * 
 * public void setCobroDestinatario(boolean cobroDestinatario) {
 * this.cobroDestinatario = cobroDestinatario;
 * }
 * 
 * public Categoria getCategoria() {
 * return categoria;
 * }
 * 
 * public void setCategoria(Categoria categoria) {
 * this.categoria = categoria;
 * }
 * 
 * public EstadoRastreo getEstadoRastreo() {
 * return estadoRastreo;
 * }
 * 
 * public void setEstadoRastreo(EstadoRastreo estadoRastreo) {
 * this.estadoRastreo = estadoRastreo;
 * }
 * 
 * public LocalDateTime getFechaHoraRegistro() {
 * return fechaHoraRegistro;
 * }
 * 
 * public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
 * this.fechaHoraRegistro = fechaHoraRegistro;
 * }
 * 
 * }
 */