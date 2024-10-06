package com.example.envios_bios.servicios;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;


public interface IServicioCliente {
    
    Cliente login(String nombreUsuario, String claveDeAcceso);
    void editarDatos(Cliente cliente) throws ExcepcionEnviosBios;
    void registrarse(Cliente cliente) throws ExcepcionEnviosBios;
    void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios;
}
