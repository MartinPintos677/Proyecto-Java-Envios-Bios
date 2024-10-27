package com.example.envios_bios.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioCategorias;
import com.example.envios_bios.repositorio.IRepositorioPaquete;

@Service
public class ServicioCategoria implements IServicioCategorias {

    @Autowired
    private IRepositorioCategorias repositorioCategorias;

    @Autowired
    private IRepositorioPaquete repositorioPaquetes;

    @Override
    public List<Categoria> listar() {
        return repositorioCategorias.findAll();
    }

    @Override
    public Categoria obtener(Integer idCat) {
        return repositorioCategorias.findById(idCat).orElse(null);
    }

    @Override
    public List<Categoria> buscar(String criterio) {

        return repositorioCategorias.findAll();
    }

    @Override
    public void agregar(Categoria categoria) throws ExcepcionEnviosBios {
        Categoria categoriaExistente = repositorioCategorias.findByNombre(categoria.getNombre());

        if (categoriaExistente != null) { // Si la encuentra, significa que ya existe
            throw new ExcepcionYaExiste("La categoría ya existe.");
        }

        // Guardamos la nueva categoría (el ID se generará automáticamente)
        repositorioCategorias.save(categoria);
    }

    @Override
    public void modificar(Categoria categoria) throws ExcepcionEnviosBios {
        // Buscar la categoría actual por idCat
        Categoria categoriaExistente = repositorioCategorias.findById(categoria.getIdCat()).orElse(null);

        if (categoriaExistente == null) {
            throw new ExcepcionNoExiste("La categoría no existe.");
        }

        // Verificar si el nuevo nombre ya está siendo usado por otra categoría
        Categoria otraCategoriaConMismoNombre = repositorioCategorias.findByNombre(categoria.getNombre());
        if (otraCategoriaConMismoNombre != null
                && !otraCategoriaConMismoNombre.getIdCat().equals(categoria.getIdCat())) {
            throw new ExcepcionYaExiste("Ya existe otra categoría con el mismo nombre.");
        }

        // Si no hay conflictos, actualizamos y guardamos
        repositorioCategorias.save(categoria);
    }

    @Override
    public void eliminar(Integer idCat) throws ExcepcionEnviosBios {
        Categoria categoriaExistente = repositorioCategorias.findById(idCat).orElse(null);

        if (categoriaExistente == null) {
            throw new ExcepcionNoExiste("La categoría no existe.");
        }

        // Verificar si hay paquetes asociados a la categoría
        boolean tienePaquetesAsociados = repositorioPaquetes.existsByCategoria(categoriaExistente);

        if (tienePaquetesAsociados) {
            throw new ExcepcionEnviosBios(
                    "No se puede eliminar la categoría porque está asociada a uno o más paquetes.");
        }

        repositorioCategorias.deleteById(idCat);
    }

    @Override
    public Page<Categoria> listarPaginado(Pageable pageable) {
        return repositorioCategorias.findAll(pageable);
    }

    @Override
    public Page<Categoria> buscarPaginado(String criterio, Pageable pageable) {
        return repositorioCategorias.buscarP(criterio, pageable);
    }

}
