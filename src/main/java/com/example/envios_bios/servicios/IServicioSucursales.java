package com.example.envios_bios.servicios;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.envios_bios.dominio.Sucursal;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;

public interface IServicioSucursales {

    List<Sucursal> listar();

    Page<Sucursal> listarPaginacion(Pageable pageable);

    Page<Sucursal> buscar(String criterio, Pageable pageable);

    Page<Sucursal> buscarPaginado(String criterio, Pageable pageable);

    Sucursal obtener(Long numero);

    void agregar(Sucursal sucursal) throws ExcepcionEnviosBios;

    void modificar(Sucursal sucursal) throws ExcepcionEnviosBios;

    void eliminar(Long numero) throws ExcepcionEnviosBios;
}
