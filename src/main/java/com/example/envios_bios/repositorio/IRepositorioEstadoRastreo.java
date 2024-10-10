package com.example.envios_bios.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.envios_bios.dominio.EstadoRastreo;

public interface IRepositorioEstadoRastreo extends JpaRepository<EstadoRastreo,Integer>{
    
    // Paginación y búsqueda por idCat (como string) o por nombre

    @Query("SELECT r FROM estado_rastreo r WHERE cast(r.idRastreo as string) = ?1 OR c.nombre LIKE concat('%', ?1, '%')")
    Page<EstadoRastreo> buscarP(String criterio, Pageable pageable);
}
