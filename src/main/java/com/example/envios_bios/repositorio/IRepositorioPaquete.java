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

        Page<Paquete> findByEstadoRastreo_DescripcionInAndCliente_CedulaAndFechaHoraRegistro(
                        List<String> descripcionEstados,
                        String cedulaCliente,
                        LocalDateTime fechaHoraRegistro,
                        Pageable pageable);

        Page<Paquete> findByEstadoRastreo_DescripcionInAndCliente_Cedula(
                        List<String> descripcionEstados,
                        String cedulaCliente,
                        Pageable pageable);

        Page<Paquete> findByEstadoRastreo_DescripcionInAndFechaHoraRegistro(
                        List<String> descripcionEstados,
                        LocalDateTime fechaHoraRegistro,
                        Pageable pageable);

        Page<Paquete> findByEstadoRastreo_DescripcionIn(
                        List<String> descripcionEstados,
                        Pageable pageable);

        Page<Paquete> findByEstadoRastreo_DescripcionInAndCliente_CedulaAndFechaHoraRegistroBetween(
                        List<String> descripcionEstados,
                        String cedulaCliente,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

        Page<Paquete> findByEstadoRastreo_DescripcionInAndFechaHoraRegistroBetween(
                        List<String> descripcionEstados,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaFin,
                        Pageable pageable);

}
