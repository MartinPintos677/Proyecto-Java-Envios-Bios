package com.example.envios_bios.servicios;

import java.util.List;

import com.example.envios_bios.dominio.Sucursal;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;

public interface IServicioSucursales {
    
    List<Sucursal> listar();
    List<Sucursal> buscar(String criterio);
    Sucursal obtener(Long numero);
    void agregar(Sucursal sucursal) throws ExcepcionEnviosBios;
    void modificar(Sucursal sucursal) throws ExcepcionEnviosBios;
    void eliminar(Long numero) throws ExcepcionEnviosBios;
}
