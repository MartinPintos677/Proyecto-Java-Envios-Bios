package com.example.envios_bios.servicios;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.dominio.Rol;
import com.example.envios_bios.dominio.Usuario;
import com.example.envios_bios.repositorio.IRepositorioClientes;
import com.example.envios_bios.repositorio.IRepositorioEmpleados;

@Service
public class ServicioDetallesUsuario implements UserDetailsService {

    @Autowired
    private IRepositorioEmpleados repositorioEmpleados;

    @Autowired
    private IRepositorioClientes repositorioClientes;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        // Intenta encontrar el usuario en el repositorio de empleados
        Optional<Empleado> empleadoOptional = repositorioEmpleados.findById(username);
    
        // Intenta encontrar el usuario en el repositorio de clientes
        Optional<Cliente> clienteOptional = repositorioClientes.findById(username);
    
        // Combina los resultados
        Usuario usuario = empleadoOptional.map(e -> (Usuario) e).orElseGet(() -> clienteOptional.map(c -> (Usuario) c).orElse(null));
    
        // Si sigue siendo nulo, lanza la excepción
        if (usuario == null) {
            throw new UsernameNotFoundException("El usuario no existe.");
        }
    
        // Verifica si el usuario es un cliente y está activo
        if (usuario instanceof Cliente) {
            Cliente cli = (Cliente) usuario;
            if (!cli.isActivo()) {
                throw new UsernameNotFoundException("El cliente no está activo.");
            }
        }
    
        // Crea el conjunto de roles
        Set<GrantedAuthority> roles = new HashSet<>();
        if (usuario.getRoles() != null) {
            for (Rol r : usuario.getRoles()) {
                roles.add(new SimpleGrantedAuthority(r.getNombreRol()));
            }
        }
        // Retorna el objeto User
        return new User(usuario.getNombreUsuario(), usuario.getClaveDeAcceso(), true, true, true, true, roles);
    }
    
}
