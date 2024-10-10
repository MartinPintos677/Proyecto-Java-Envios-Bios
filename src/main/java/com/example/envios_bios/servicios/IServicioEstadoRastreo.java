package com.example.envios_bios.servicios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;

public interface IServicioEstadoRastreo {
    
    List<EstadoRastreo> listar();
    List<EstadoRastreo> buscar(String criterio);
    EstadoRastreo obtener(Integer idRastreo);
    void agregar(EstadoRastreo rastreo) throws ExcepcionEnviosBios;
    void modificar(EstadoRastreo rastreo) throws ExcepcionEnviosBios;
    void eliminar(Integer idRastreo) throws ExcepcionEnviosBios;
    // Paginación
    Page<EstadoRastreo> listarPaginado(Pageable pageable);
    Page<EstadoRastreo> buscarPaginado(String criterio, Pageable pageable);
}
