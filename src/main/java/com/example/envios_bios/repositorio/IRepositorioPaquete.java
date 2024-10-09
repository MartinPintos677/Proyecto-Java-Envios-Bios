package com.example.envios_bios.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import com.example.envios_bios.dominio.Paquete;

public interface IRepositorioPaquete extends JpaRepository<Paquete, Long>, JpaSpecificationExecutor<Paquete>{

    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "cliente","categoria","estadoRastreo" })
    List<Paquete> findAll();

    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "cliente","categoria","estadoRastreo" })
    Page<Paquete> findAll(Pageable pageable);

    //comentar si no usamos Specification
    // @Override
    // @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "cliente","categoria","estadoRastreo" })
    // Page<Paquete> findAll(@Nullable Specification<Paquete> spec, Pageable pageable);

    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "cliente","categoria","estadoRastreo" })
    Optional<Paquete> findById(Long id);
    
}
