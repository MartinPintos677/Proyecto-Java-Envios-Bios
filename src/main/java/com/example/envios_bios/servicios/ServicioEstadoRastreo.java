package com.example.envios_bios.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;

@Service
public class ServicioEstadoRastreo implements IServicioEstadoRastreo{
    
    private List<EstadoRastreo> rastreos;       
        

    public ServicioEstadoRastreo(){ 
       rastreos = new ArrayList<>();

        //datos de prueba
        rastreos.add(new EstadoRastreo(1,"estRastreo1", true));
        rastreos.add(new EstadoRastreo(2,"estRastreo2",true));
        rastreos.add(new EstadoRastreo(3,"estRastreo3",true));
        rastreos.add(new EstadoRastreo(4,"estRastreo4",true));
        rastreos.add(new EstadoRastreo(5,"estRastreo5",true));        
        
    }

    @Override
    public List<EstadoRastreo> listar(){
        return new ArrayList<>(rastreos);
    }

    @Override
    public EstadoRastreo obtener(Integer idRastreo){
        EstadoRastreo rastreoEncontrado = null;

        for (EstadoRastreo r : rastreos){
            if (r.getIdRastreo() == idRastreo) {
                rastreoEncontrado = r;
                break;
            }
        }

        return rastreoEncontrado;
    }

    private int obtenerPosicion(Integer idRastreo){
        for (int i = 0; i < rastreos.size(); i++){
            if (rastreos.get(i).getIdRastreo().equals(idRastreo)) { 
                return i;
            }
        }
        return -1; // No se encontró la posición
    }

    @Override
    public List<EstadoRastreo> buscar(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return listar();
        }

        criterio = criterio.trim().toLowerCase();
        List<EstadoRastreo> rastreoFiltrados = new ArrayList<>();

        for (EstadoRastreo r : this.rastreos){
            if (r.getIdRastreo().toString().equals(criterio) || r.getDescripcion().toLowerCase().contains(criterio)) {
                rastreoFiltrados.add(r);
            }
        }

        return rastreoFiltrados;
    }

    @Override
    public void agregar(EstadoRastreo rastreo) throws ExcepcionEnviosBios {
        
        if (obtener(rastreo.getIdRastreo()) != null) {
            throw new ExcepcionYaExiste("El estado de Rastreo con ID " + rastreo.getIdRastreo() + " ya existe.");
        }

        // Si no existe, agregamos el nuevo estado
        rastreos.add(rastreo);
    }

    @Override
    public void modificar(EstadoRastreo rastreo) throws ExcepcionEnviosBios {
        int posicion = obtenerPosicion(rastreo.getIdRastreo());
        if (posicion == -1) {
            throw new ExcepcionEnviosBios("El estado de rastreo a modificar no existe.");
        }

        // Se modifica el estado en la posición encontrada
        rastreos.set(posicion, rastreo);
    }

    @Override
    public void eliminar(Integer idRastreo) throws ExcepcionEnviosBios {
        int posicion = obtenerPosicion(idRastreo);
        if (posicion == -1) {
            throw new ExcepcionEnviosBios("El estado a eliminar no existe.");
        }

        // Se elimina el estado de la lista
        rastreos.remove(posicion);
    }
}
