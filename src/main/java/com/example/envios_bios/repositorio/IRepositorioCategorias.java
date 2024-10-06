package com.example.envios_bios.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.envios_bios.dominio.Categoria;

public interface IRepositorioCategorias extends JpaRepository<Categoria,Integer>{
    
}
