package com.example.envios_bios.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Sucursal;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;

@Service
public class ServicioSucursales implements IServicioSucursales{
    
    //private ArrayList<Sucursal> sucursales; 
    private List<Sucursal> sucursales;       
        

    public ServicioSucursales(){ 
       sucursales = new ArrayList<>();

        //datos de prueba
        sucursales.add(new Sucursal(1L,"sucursal1"));
        sucursales.add(new Sucursal(2L,"sucursal2"));
        sucursales.add(new Sucursal(3L,"sucursal3"));
        sucursales.add(new Sucursal(4L,"sucursal4"));
        sucursales.add(new Sucursal(5L,"sucursal5"));        
        
    }

    @Override
    public List<Sucursal> listar(){
        return new ArrayList<>(sucursales);
    }

    @Override
    public Sucursal obtener(Long numero){
        Sucursal sucursalEncontrada = null;

        for (Sucursal s : sucursales){
            if (s.getNumero().equals(numero)) {
                sucursalEncontrada = s;
                break;
            }
        }

        return sucursalEncontrada;
    }

    private int obtenerPosicion(Long numero){
        for (int i = 0; i < sucursales.size(); i++){
            if (sucursales.get(i).getNumero().equals(numero)) { 
                return i;
            }
        }
        return -1; // No se encontró la posición
    }

    @Override
    public List<Sucursal> buscar(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return listar();
        }

        criterio = criterio.trim().toLowerCase();
        List<Sucursal> sucursalesFiltradas = new ArrayList<>();

        for (Sucursal s : this.sucursales){
            if (s.getNumero().toString().equals(criterio) || s.getNombre().toLowerCase().contains(criterio)) {
                sucursalesFiltradas.add(s);
            }
        }

        return sucursalesFiltradas;
    }

    @Override
    public void agregar(Sucursal sucursal) throws ExcepcionEnviosBios {
        // Verificamos si la sucursal ya existe en la lista por su ID o nombre
        if (obtener(sucursal.getNumero()) != null) {
            throw new ExcepcionYaExiste("La sucursal con ID " + sucursal.getNumero() + " ya existe.");
        }

        // Si no existe, agregamos la nueva sucursal
        sucursales.add(sucursal);
    }

    @Override
    public void modificar(Sucursal sucursal) throws ExcepcionEnviosBios {
        int posicion = obtenerPosicion(sucursal.getNumero());
        if (posicion == -1) {
            throw new ExcepcionEnviosBios("La sucursal a modificar no existe.");
        }

        // Se modifica la categoría en la posición encontrada
        sucursales.set(posicion, sucursal);
    }

    @Override
    public void eliminar(Long numero) throws ExcepcionEnviosBios {
        int posicion = obtenerPosicion(numero);
        if (posicion == -1) {
            throw new ExcepcionEnviosBios("La sucursal a eliminar no existe.");
        }

        // Se elimina la categoría de la lista
        sucursales.remove(posicion);
    }
}
