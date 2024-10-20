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
import com.example.envios_bios.repositorio.IRepositorioPaquete;

@Service
public class ServicioEstadoRastreo implements IServicioEstadoRastreo {

    @Autowired
    private IRepositorioEstadoRastreo repositorioEstadoRastreo;

    @Autowired
    private IRepositorioPaquete repositorioPaquetes;

    @Override
    public List<EstadoRastreo> listar() {
        return repositorioEstadoRastreo.findAllActivos();
    }

    @Override
    public EstadoRastreo obtener(Integer idRastreo) {
        return repositorioEstadoRastreo.findById(idRastreo).orElse(null);
    }

    @Override
    public List<EstadoRastreo> buscar(String criterio) {
        return repositorioEstadoRastreo.findAllActivos();
    }

    @Override
    public void agregar(EstadoRastreo rastreo) throws ExcepcionEnviosBios {
        // Verificar si ya existe un estado con la misma descripción, sea activo o
        // inactivo
        EstadoRastreo rastreoExistente = repositorioEstadoRastreo.findByDescripcion(rastreo.getDescripcion());

        if (rastreoExistente != null) {
            if (!rastreoExistente.isActivo()) {
                // Si ya existe y está inactivo, lo volvemos a activar
                rastreoExistente.setActivo(true);
                repositorioEstadoRastreo.save(rastreoExistente);
                return; // Finalizamos aquí ya que reactivamos el estado
            } else {
                throw new ExcepcionYaExiste("Ya existe un Estado de Rastreo con esa descripción y está activo.");
            }
        }

        // Si no existe, lo guardamos como un nuevo estado
        rastreo.setActivo(true);
        repositorioEstadoRastreo.save(rastreo);
    }

    @Override
    public void modificar(EstadoRastreo rastreo) throws ExcepcionEnviosBios {
        // busco por el idRastreo
        EstadoRastreo rastreoExistente = repositorioEstadoRastreo.findById(rastreo.getIdRastreo()).orElse(null);

        if (rastreoExistente == null) {
            throw new ExcepcionNoExiste("El estado de Rastreo no existe.");
        }

        rastreo.setActivo(true);
        repositorioEstadoRastreo.save(rastreo);
    }

    @Override
    public void eliminar(Integer idRastreo) throws ExcepcionEnviosBios {
        EstadoRastreo rastreoExistente = repositorioEstadoRastreo.findById(idRastreo).orElse(null);

        if (rastreoExistente == null) {
            throw new ExcepcionNoExiste("El estado de Rastreo no existe.");
        }

        // Verificar si hay paquetes asociados a este estado de rastreo
        boolean tienePaquetesAsociados = repositorioPaquetes.existsByEstadoRastreo(rastreoExistente);

        if (tienePaquetesAsociados) {
            // Si tiene paquetes, realizamos la baja lógica
            rastreoExistente.setActivo(false);
            repositorioEstadoRastreo.save(rastreoExistente);
        } else {
            // Si no tiene paquetes, lo eliminamos físicamente
            repositorioEstadoRastreo.deleteById(idRastreo);
        }
    }

    @Override
    public Page<EstadoRastreo> listarPaginado(Pageable pageable) {
        return repositorioEstadoRastreo.buscarPagina("", pageable); // Busca solo los activos
    }

    @Override
    public Page<EstadoRastreo> buscarPaginado(String criterio, Pageable pageable) {
        return repositorioEstadoRastreo.buscarPagina(criterio, pageable); // Busca con paginación
    }
}
