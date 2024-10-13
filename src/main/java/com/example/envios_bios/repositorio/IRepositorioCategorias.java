package com.example.envios_bios.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.envios_bios.dominio.Categoria;

public interface IRepositorioCategorias extends JpaRepository<Categoria, Integer> {

    // Paginación y búsqueda por idCat (como string) o por nombre

    @Query("SELECT c FROM Categoria c WHERE cast(c.idCat as string) = ?1 OR c.nombre LIKE concat('%', ?1, '%')")
    Page<Categoria> buscarP(String criterio, Pageable pageable);

    Categoria findByNombre(String nombre); // Este método buscará categorías por su nombre

}
