package com.example.envios_bios.repositorio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.envios_bios.dominio.Sucursal;

public interface IRepositorioSucursal extends JpaRepository<Sucursal, Long>, JpaSpecificationExecutor<Sucursal> {

    List<Sucursal> findByNombreContaining(String criterio);

    Page<Sucursal> findByNombreContaining(String criterio, Pageable pageable);

}
