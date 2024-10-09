package com.example.envios_bios.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.envios_bios.dominio.Cliente;

public interface IRepositorioClientes extends JpaRepository<Cliente, String>{
    
    //trae los clientes activos - ver si corresponde
    //List<Cliente> findByActivo(boolean activo);



    //En caso de no usar Roles, comentar esto
    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "roles"})//Se carga con los roles
    List<Cliente> findAll();

    @Override
    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "roles"})//Se carga con los roles
    Optional<Cliente> findById(String id);
}
