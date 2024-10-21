package com.example.envios_bios.repositorio;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.dominio.Paquete;

public interface IRepositorioPaquete extends JpaRepository<Paquete, Long> {

        boolean existsByCategoria(Categoria categoria);

        boolean existsByEstadoRastreo(EstadoRastreo estadoRastreo);

        Page<Paquete> findByCliente_NombreUsuarioAndNombreDestinatarioContaining(String cliente, String destinatario, Pageable pageable);

        Page<Paquete> findByCliente_NombreUsuario(String cliente, Pageable pageable);
        
        // Filtro por estado de rastreo (lista) y cédula
        Page<Paquete> findByEstadoRastreo_DescripcionInAndCliente_CedulaAndFechaHoraRegistroBetween(
                        List<String> descripcionEstados,
                        String cedulaCliente,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

        // Filtro por estado de rastreo (único) y cédula
        Page<Paquete> findByEstadoRastreo_DescripcionAndCliente_CedulaAndFechaHoraRegistroBetween(
                        String estadoRastreo,
                        String cedulaCliente,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

        // Filtro por estado de rastreo (lista) y cédula sin fecha
        Page<Paquete> findByEstadoRastreo_DescripcionInAndCliente_Cedula(
                        List<String> descripcionEstados,
                        String cedulaCliente,
                        Pageable pageable);

        // Filtro por estado de rastreo (único) y cédula sin fecha
        Page<Paquete> findByEstadoRastreo_DescripcionAndCliente_Cedula(
                        String estadoRastreo,
                        String cedulaCliente,
                        Pageable pageable);

        // Filtro por estado de rastreo (lista) y fecha de registro
        Page<Paquete> findByEstadoRastreo_DescripcionInAndFechaHoraRegistroBetween(
                        List<String> descripcionEstados,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

        // Filtro por estado de rastreo (único) y fecha de registro
        Page<Paquete> findByEstadoRastreo_DescripcionAndFechaHoraRegistroBetween(
                        String estadoRastreo,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

        // Filtro por estado de rastreo (único)
        Page<Paquete> findByEstadoRastreo_Descripcion(
                        String estadoRastreo,
                        Pageable pageable);

        // Filtro por cédula del cliente
        Page<Paquete> findByCliente_Cedula(
                        String cedulaCliente,
                        Pageable pageable);

        // Filtro por cédula del cliente y fecha de registro
        Page<Paquete> findByCliente_CedulaAndFechaHoraRegistroBetween(
                        String cedulaCliente,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

        // Filtro solo por rango de fechas
        Page<Paquete> findByFechaHoraRegistroBetween(
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

}
