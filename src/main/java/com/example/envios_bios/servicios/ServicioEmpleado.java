package com.example.envios_bios.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.dominio.Rol;
import com.example.envios_bios.dominio.Usuario;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioEmpleados;

@Service
public class ServicioEmpleado implements IServicioEmpleado{
    
    @Autowired
    private IRepositorioEmpleados repositorioEmpleados;

    @Override
    public List<Empleado> buscar(String criterio) {
        return repositorioEmpleados.findAll();
    }

    @Override
    public Empleado obtener(String nombreUsuario) {
        return repositorioEmpleados.findById(nombreUsuario).orElse(null);
    }

    @Override
    public void agregar(Empleado empleado) throws ExcepcionEnviosBios {
        Usuario usuarioExistente = repositorioEmpleados.findById(empleado.getNombreUsuario()).orElse(null);

        if (usuarioExistente != null) {
            throw new ExcepcionYaExiste("El empleado ya existe.");
        }

        empleado.getRoles().add(new Rol("empleado"));

        repositorioEmpleados.save(empleado);
    }

    @Override
    public void modificar(Empleado empleado) throws ExcepcionEnviosBios {

        Empleado empleadoExistente = repositorioEmpleados.findById(empleado.getNombreUsuario()).orElse(null);

        if (empleadoExistente == null) {
            throw new ExcepcionNoExiste("El empleado no existe.");
        }

        empleado.getRoles().clear();//Limpiamos los roles que tenga

        for (Rol r : empleadoExistente.getRoles()) { //Se los agregamos
            empleado.getRoles().add(r);
        }

        repositorioEmpleados.save(empleado);

    }


    @Override
    public void eliminar(String nombreUsuario) throws ExcepcionEnviosBios {

        Empleado empleadoExistente = repositorioEmpleados.findById(nombreUsuario).orElse(null);

        if (empleadoExistente == null) {
            throw new ExcepcionNoExiste("El empleado no existe.");
        }

        repositorioEmpleados.deleteById(nombreUsuario);
        
    }
}
