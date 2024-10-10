package com.example.envios_bios.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioEstadoRastreo;

@Service
public class ServicioEstadoRastreo implements IServicioEstadoRastreo{
    
    @Autowired
    private IRepositorioEstadoRastreo repositorioEstadoRastreo;     
        
    
    @Override
    public List<EstadoRastreo> listar(){
        return repositorioEstadoRastreo.findAll();
    }

    @Override
    public EstadoRastreo obtener(Integer idRastreo){
        return repositorioEstadoRastreo.findById(idRastreo).orElse(null);        
    }
    
    @Override
    public List<EstadoRastreo> buscar(String criterio) {
        return repositorioEstadoRastreo.findAll();       
       
    }

    @Override
    public void agregar(EstadoRastreo rastreo) throws ExcepcionEnviosBios {
        
       // busco por el idRastreo
        EstadoRastreo rastreoExistente = repositorioEstadoRastreo.findById(rastreo.getIdRastreo()).orElse(null);

        if (rastreoExistente != null) { //si la encuentra muestra el mensaje de error
            throw new ExcepcionYaExiste("El estado de Rastreo ya existe.");
        }

        repositorioEstadoRastreo.save(rastreo); //guardamos en la bd
    }

    @Override
    public void modificar(EstadoRastreo rastreo) throws ExcepcionEnviosBios {
        // busco por el idRastreo
        EstadoRastreo rastreoExistente = repositorioEstadoRastreo.findById(rastreo.getIdRastreo()).orElse(null);

        if (rastreoExistente == null) {
            throw new ExcepcionNoExiste("El estado de Rastreo no existe.");
        }

        repositorioEstadoRastreo.save(rastreo);
    }

    @Override
    public void eliminar(Integer idRastreo) throws ExcepcionEnviosBios {
        EstadoRastreo rastreoExistente = repositorioEstadoRastreo.findById(idRastreo).orElse(null);

        if (rastreoExistente == null) {
            throw new ExcepcionNoExiste("El estado de Rastreo no existe.");
        }
        repositorioEstadoRastreo.deleteById(idRastreo);
    }
/* 
    @Override
    public Page<EstadoRastreo> listarPaginado(Pageable pageable) {
        return repositorioEstadoRastreo.findAll(pageable); 
    }

    @Override
    public Page<EstadoRastreo> buscarPaginado(String criterio, Pageable pageable) {
        return repositorioEstadoRastreo.buscarPagina(criterio, pageable);
    }
    */
}
