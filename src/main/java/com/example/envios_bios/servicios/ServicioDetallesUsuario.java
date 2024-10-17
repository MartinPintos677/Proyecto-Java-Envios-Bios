package com.example.envios_bios.servicios;

import java.util.HashSet;
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
import com.example.envios_bios.dominio.Rol;
import com.example.envios_bios.dominio.Usuario;
import com.example.envios_bios.repositorio.IRepositorioClientes;
import com.example.envios_bios.repositorio.IRepositorioEmpleados;

@Service
public class ServicioDetallesUsuario implements UserDetailsService{
    
    @Autowired
    private IRepositorioEmpleados repositorioEmpleados;

    @Autowired
    private IRepositorioClientes repositorioClientes;


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Usuario usuario = repositorioEmpleados.findById(username).orElse(null);

        if (usuario == null) {
            usuario = repositorioClientes.findById(username).orElse(null);
            Cliente cli = (Cliente)usuario;
            if(usuario == null || cli == null || !cli.isActivo()) throw new UsernameNotFoundException("El usuario no existe.");
        }

        Set<GrantedAuthority> roles = new HashSet<>();

        for (Rol r : usuario.getRoles()) {
            roles.add(new SimpleGrantedAuthority(r.getNombreRol()));
        }

        return new User(usuario.getNombreUsuario(), usuario.getClaveDeAcceso(), true, true, true,true,roles);
    }
}
