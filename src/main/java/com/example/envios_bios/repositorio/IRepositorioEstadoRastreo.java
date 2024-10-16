package com.example.envios_bios.repositorio;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.envios_bios.dominio.EstadoRastreo;

public interface IRepositorioEstadoRastreo extends JpaRepository<EstadoRastreo, Integer> {

    // Paginación y búsqueda por idRastreo (como string) o por descripcion, solo
    // activos
    @Query("SELECT r FROM EstadoRastreo r WHERE (cast(r.idRastreo as string) = ?1 OR r.descripcion LIKE concat('%', ?1, '%')) AND r.activo = true")
    Page<EstadoRastreo> buscarPagina(String criterio, Pageable pageable);

    // Buscar por descripción solo si está activo
    EstadoRastreo findByDescripcionAndActivoTrue(String descripcion);

    EstadoRastreo findByDescripcion(String descripcion);

    // Método para obtener todos los estados activos
    @Query("SELECT r FROM EstadoRastreo r WHERE r.activo = true")
    List<EstadoRastreo> findAllActivos();
}
