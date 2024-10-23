package com.example.envios_bios.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.dominio.Rol;
import com.example.envios_bios.dominio.Usuario;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioEmpleados;

@Service
public class ServicioEmpleado implements IServicioEmpleado {

    @Autowired
    private IRepositorioEmpleados repositorioEmpleados;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<Empleado> listarPaginado(Pageable pageable) {
        return repositorioEmpleados.findAll(pageable);
    }

    @Override
    public Page<Empleado> buscarConPaginacion(String criterio, Pageable pageable) {
        if (criterio == null || criterio.isEmpty()) {
            return repositorioEmpleados.findAll(pageable);
        } else {
            return repositorioEmpleados.findByNombreUsuarioContainingIgnoreCase(criterio, pageable);
        }
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

        // Encriptar la contraseña antes de guardarla
        String claveEncriptada = passwordEncoder.encode(empleado.getClaveDeAcceso());
        empleado.setClaveDeAcceso(claveEncriptada);

        empleado.getRoles().add(new Rol("empleado"));

        repositorioEmpleados.save(empleado);
    }

    @Override
    public void modificar(Empleado empleado) throws ExcepcionEnviosBios {

        Empleado empleadoExistente = repositorioEmpleados.findById(empleado.getNombreUsuario()).orElse(null);

        if (empleadoExistente == null) {
            throw new ExcepcionNoExiste("El empleado no existe.");
        }

        // Si la contraseña fue modificada (es diferente de la actual), encripta la
        // nueva clave
        if (!empleado.getClaveDeAcceso().equals(empleadoExistente.getClaveDeAcceso())) {
            String claveEncriptada = passwordEncoder.encode(empleado.getClaveDeAcceso());
            empleado.setClaveDeAcceso(claveEncriptada);
        } else {
            // Mantener la clave original si no fue modificada
            empleado.setClaveDeAcceso(empleadoExistente.getClaveDeAcceso());
        }

        empleado.getRoles().clear();// Limpiamos los roles que tenga

        for (Rol r : empleadoExistente.getRoles()) { // Se los agregamos
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
