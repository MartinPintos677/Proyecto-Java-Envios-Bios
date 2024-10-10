package com.example.envios_bios.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.envios_bios.dominio.EstadoRastreo;

public interface IRepositorioEstadoRastreo extends JpaRepository<EstadoRastreo,Integer>{
    
    // Paginación y búsqueda por idRastreo (como string) o por descripcion
/* 
    @Query("SELECT r FROM estadoRastreo r WHERE cast(r.idRastreo as string) = ?1 OR r.descripcion LIKE concat('%', ?1, '%')")
    Page<EstadoRastreo> buscarPagina(String criterio, Pageable pageable);
    */
}
