package com.example.envios_bios.servicios;

import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.dominio.Rol;
import com.example.envios_bios.dominio.Usuario;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioClientes;
import com.example.envios_bios.repositorio.IRepositorioPaquete;

@Service
public class ServicioCliente implements IServicioCliente{

    @Autowired
    private IRepositorioClientes repositorioClientes;

    @Autowired
    private IRepositorioPaquete repositorioPaquete;

    @Override
    public Cliente obtener(String nombreUsuario) {
        return repositorioClientes.findById(nombreUsuario).orElse(null);
    }
    
    @Override
    public void modificar(Cliente cliente) throws ExcepcionEnviosBios {
        Cliente clienteExistente = repositorioClientes.findById(cliente.getNombreUsuario()).orElse(null);

        if (clienteExistente == null) {
            throw new ExcepcionNoExiste("El Cliente no existe.");
        }

        cliente.getRoles().clear();//Limpiamos los roles que tenga

        cliente.setActivo(true);

        for (Rol r : clienteExistente.getRoles()) { //Se los agregamos
            cliente.getRoles().add(r);
        }

        repositorioClientes.save(cliente);
    }

    @Override
    public void registrarse(Cliente cliente) throws ExcepcionEnviosBios {
        Usuario usuarioExistente = repositorioClientes.findById(cliente.getNombreUsuario()).orElse(null);

        if (usuarioExistente != null) {
            throw new ExcepcionYaExiste("El Cliente ya existe.");
        }

        cliente.getRoles().add(new Rol("cliente"));//Le damos el rol

        repositorioClientes.save(cliente); //Registramos el Cliente
    }

    @Override
    public void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
        Paquete paqueteExistente = repositorioPaquete.findById(paquete.getIdPaquete()).orElse(null);

        if (paqueteExistente != null) {
            throw new ExcepcionYaExiste("El Paquete ya existe.");
        }

        paquete.setFechaHoraRegistro(LocalDateTime.now());//Seteamos la fecha actual

        repositorioPaquete.save(paquete);
    }

    @Override
    public void eliminar(String nombreUsuario) throws ExcepcionEnviosBios {
        
        Cliente clienteExistente = repositorioClientes.findById(nombreUsuario).orElse(null);
        if (clienteExistente == null) {
            throw new ExcepcionNoExiste("El cliente no existe.");
        }

        boolean existePaqueteConCliente = repositorioPaquete.findAll().stream().anyMatch(p -> p.getCliente().getNombreUsuario().equals(clienteExistente.getNombreUsuario()));
        if (existePaqueteConCliente) {
            clienteExistente.setActivo(false);
            repositorioClientes.save(clienteExistente);//Lo guardamos pero con el activo en false
        }
        else{
            repositorioClientes.deleteById(nombreUsuario);
        }
    }

    

    
}
