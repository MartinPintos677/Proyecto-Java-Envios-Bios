package com.example.envios_bios.repositorio;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.dominio.Sucursal;

public interface IRepositorioEmpleados extends JpaRepository<Empleado, String>, JpaSpecificationExecutor<Empleado> {

    boolean existsBySucursal(Sucursal sucursal);

    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "roles", "sucursal" })
    List<Empleado> findAll();

    // @Override
    // @EntityGraph(type = EntityGraphType.LOAD, attributePaths = {
    // "roles","sucursal"})
    // List<Empleado> findAll(@Nullable Specification<Empleado> spec);

    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "roles", "sucursal" })
    Optional<Empleado> findById(String id);

}
