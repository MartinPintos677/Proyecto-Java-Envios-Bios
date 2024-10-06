package com.example.envios_bios.repositorio;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.example.envios_bios.dominio.Sucursal;

public interface IRepositorioSucursal extends JpaRepository<Sucursal,Long>, JpaSpecificationExecutor<Sucursal>{
    
    //comentar si no usamos Specification
    @Override
    List<Sucursal> findAll(@Nullable Specification<Sucursal> spec);
    
}
