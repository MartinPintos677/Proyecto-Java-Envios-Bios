package com.example.envios_bios.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Sucursal;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioSucursal;

@Service
public class ServicioSucursales implements IServicioSucursales{

    @Autowired
    private IRepositorioSucursal repositorioSucursal; 

    @Override
    public List<Sucursal> listar(){
        return repositorioSucursal.findAll();
    }

    @Override
    public Sucursal obtener(Long numero){
        return repositorioSucursal.findById(numero).orElse(null);
    }

    @Override
    public List<Sucursal> buscar(String criterio) {
        return repositorioSucursal.findAll(); //En caso de usar Specifications pasarle el criterio
    }

    @Override
    public void agregar(Sucursal sucursal) throws ExcepcionEnviosBios {
        
        Sucursal s = repositorioSucursal.findById(sucursal.getNumero()).orElse(null);//Buscamos la sucursal

        if (s != null) { //Si la encuentra, tira mensaje de error
            throw new ExcepcionYaExiste("La Sucursal ya existe.");
        }

        repositorioSucursal.save(sucursal);//sino, la guardamos en la BD
    }

    @Override
    public void modificar(Sucursal sucursal) throws ExcepcionEnviosBios {
        Sucursal s = repositorioSucursal.findById(sucursal.getNumero()).orElse(null);

        if (s == null) {
            throw new ExcepcionNoExiste("La Sucursal no existe.");
        }
        repositorioSucursal.save(sucursal);
    }

    @Override
    public void eliminar(Long numero) throws ExcepcionEnviosBios {
        Sucursal s = repositorioSucursal.findById(numero).orElse(null);

        if (s == null) {
            throw new ExcepcionNoExiste("La Sucursal no existe.");
        }

        repositorioSucursal.deleteById(numero);
    }
}
