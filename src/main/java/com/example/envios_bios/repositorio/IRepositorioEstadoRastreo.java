package com.example.envios_bios.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.envios_bios.dominio.EstadoRastreo;

public interface IRepositorioEstadoRastreo extends JpaRepository<EstadoRastreo,Integer>{
    
}
