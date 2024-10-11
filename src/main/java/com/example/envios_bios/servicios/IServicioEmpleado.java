package com.example.envios_bios.servicios;

import java.util.List;

import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;

public interface IServicioEmpleado {
    
    List<Empleado> buscar(String criterio);
    Empleado obtener(String nombreUsuario);
    void agregar(Empleado empleado) throws ExcepcionEnviosBios;
    void modificar(Empleado empleado) throws ExcepcionEnviosBios;
    void eliminar(String nombreUsuario) throws ExcepcionEnviosBios;
}
