package com.example.envios_bios.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.envios_bios.dominio.Categoria;

public interface IRepositorioCategorias extends JpaRepository<Categoria,Integer>{
    
    //paginacion
    @Query("Select c from categorias c where cast(idCat as string) = ?1 or nombre like concat('%', ?1, '%')")
    Page<Categoria> buscarP(String criterio, Pageable pageable);



}
