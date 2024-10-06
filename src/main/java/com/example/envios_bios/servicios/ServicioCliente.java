package com.example.envios_bios.servicios;

import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;

@Service
public class ServicioCliente implements IServicioCliente{
    
    @Override
    public Cliente login(String nombreUsuario, String claveDeAcceso) {
        //Vemos
        return new Cliente();
    }

    @Override
    public void editarDatos(Cliente cliente) throws ExcepcionEnviosBios {
        //Vemos
    }

    @Override
    public void registrarse(Cliente cliente) throws ExcepcionEnviosBios {
        //Vemos
    }

    @Override
    public void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
        //Vemos
    }
}
