package com.example.envios_bios.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;

@Service
public class ServicioCategoria implements IServicioCategorias{
    
    private List<Categoria> categorias;

    public ServicioCategoria(){
        categorias = new ArrayList<>();

        //datos de prueba
        categorias.add(new Categoria(1,"cat1"));
        categorias.add(new Categoria(2,"cat2"));
        categorias.add(new Categoria(3,"cat3"));
        categorias.add(new Categoria(4,"cat4"));
        categorias.add(new Categoria(5,"cat5"));
        
    }

    @Override
    public List<Categoria> listar(){
        return new ArrayList<>(categorias);
    }

    @Override
    public Categoria obtener(Integer idCat){
        Categoria categoriaEncontrada = null;

        for (Categoria c : categorias){
            if (c.getIdCat() == idCat) {
                categoriaEncontrada = c;
                break;
            }
        }

        return categoriaEncontrada;
    }

    private int obtenerPosicion(Integer idCat){
        for (int i = 0; i < categorias.size(); i++){
            if (categorias.get(i).getIdCat().equals(idCat)) { 
                return i;
            }
        }
        return -1; // No se encontró la posición
    }

    @Override
    public List<Categoria> buscar(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return listar();
        }

        criterio = criterio.trim().toLowerCase();
        List<Categoria> categoriasFiltradas = new ArrayList<>();

        for (Categoria c : this.categorias){
            if (c.getIdCat().toString().equals(criterio) || c.getNombre().toLowerCase().contains(criterio)) {
                categoriasFiltradas.add(c);
            }
        }

        return categoriasFiltradas;
    }

    @Override
    public void agregar(Categoria categoria) throws ExcepcionEnviosBios {
        // Verificamos si la categoría ya existe en la lista por su ID o nombre
        if (obtener(categoria.getIdCat()) != null) {
            throw new ExcepcionYaExiste("La categoría con ID " + categoria.getIdCat() + " ya existe.");
        }

        // Si no existe, agregamos la nueva categoría
        categorias.add(categoria);
    }

    @Override
    public void modificar(Categoria categoria) throws ExcepcionEnviosBios {
        int posicion = obtenerPosicion(categoria.getIdCat());
        if (posicion == -1) {
            throw new ExcepcionEnviosBios("La categoría a modificar no existe.");
        }

        // Se modifica la categoría en la posición encontrada
        categorias.set(posicion, categoria);
    }

    @Override
    public void eliminar(Integer idCat) throws ExcepcionEnviosBios {
        int posicion = obtenerPosicion(idCat);
        if (posicion == -1) {
            throw new ExcepcionEnviosBios("La categoría a eliminar no existe.");
        }

        // Se elimina la categoría de la lista
        categorias.remove(posicion);
    }
}
