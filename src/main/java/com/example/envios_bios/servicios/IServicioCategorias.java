package com.example.envios_bios.servicios;

import java.util.List;
import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServicioCategorias {

    List<Categoria> listar();

    List<Categoria> buscar(String criterio);

    Categoria obtener(Integer idCat);

    void agregar(Categoria categoria) throws ExcepcionEnviosBios;

    void modificar(Categoria categoria) throws ExcepcionEnviosBios;

    void eliminar(Integer idCat) throws ExcepcionEnviosBios;

    // Paginación
    Page<Categoria> listarPaginado(Pageable pageable);

    Page<Categoria> buscarPaginado(String criterio, Pageable pageable);
}
