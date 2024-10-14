package com.example.envios_bios.servicios;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;


public interface IServicioCliente {
    
    void modificar(Cliente cliente) throws ExcepcionEnviosBios;
    void registrarse(Cliente cliente) throws ExcepcionEnviosBios;
    void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios;
    void eliminar(String nombreUsuario) throws ExcepcionEnviosBios;
}
