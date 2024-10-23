package com.example.envios_bios.servicios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;

public interface IServicioEmpleado {

    Empleado obtener(String nombreUsuario);

    Page<Empleado> listarPaginado(Pageable pageable);

    Page<Empleado> buscarConPaginacion(String criterio, Pageable pageable);

    void agregar(Empleado empleado) throws ExcepcionEnviosBios;

    void modificar(Empleado empleado) throws ExcepcionEnviosBios;

    void eliminar(String nombreUsuario) throws ExcepcionEnviosBios;
}
