package com.example.envios_bios.servicios;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionTieneVinculos;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;
import com.example.envios_bios.repositorio.IRepositorioClientes;
import com.example.envios_bios.repositorio.IRepositorioPaquete;


@Service
public class ServicioPaquete implements IServicioPaquete{
    
 
  @Autowired
  private IRepositorioPaquete repositorioPaquete;

  @Autowired
  private IRepositorioClientes repositorioClientes;
  
  // Método para obtener la lista de clientes
  @Override
  public List<Cliente> listarClientes() {
    return repositorioClientes.findAll();
  }

  @Override
  public List<Paquete> buscar(String criterio, String cedulaCliente, String fechaRegistro, String estadoRastreo) {
    return repositorioPaquete.findAll().stream()
        // Filtro por criterio (ID, destinatario)
        .filter(p -> (criterio == null || criterio.isBlank() || p.getIdPaquete().toString().contains(criterio)
            || p.getNombreDestinatario().toLowerCase().contains(criterio)))
        // Filtro por cédula del cliente
        .filter(p -> (cedulaCliente == null || cedulaCliente.isBlank()
            || p.getCliente().getCedula().equals(cedulaCliente)))
        // Filtro por fecha de registro
        .filter(p -> (fechaRegistro == null || fechaRegistro.isBlank()
            || p.getFechaHoraRegistro().toLocalDate().toString().equals(fechaRegistro)))
        // Filtro por estado de rastreo
        .filter(p -> (estadoRastreo == null || estadoRastreo.isBlank()
            || p.getEstadoRastreo().getDescripcion().equals(estadoRastreo)))
        .collect(Collectors.toList());
  }

  @Override
  public List<Paquete> listarTodosLosPaquetes() {
    return repositorioPaquete.findAll();
  }

  @Override
  public Paquete obtenerPaquetePorId(Long idPaquete) {
    return repositorioPaquete.findById(idPaquete).orElse(null);
  }

  @Override
  public void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
     Paquete p = repositorioPaquete.findById(paquete.getIdPaquete()).orElse(null);// Buscamos el paquete

        if (p != null) { // Si la encuentra, tira mensaje de error
            throw new ExcepcionYaExiste("El paquete ya existe.");
        }

        repositorioPaquete.save(paquete);// sino, la guardamos en la BD
  }

  @Override
  public void modificarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
     Paquete p = repositorioPaquete.findById(paquete.getIdPaquete()).orElse(null);

        if (p == null) {
            throw new ExcepcionNoExiste("El paquete no existe.");
        }
        repositorioPaquete.save(paquete);
  }

  @Override
  public void eliminarPaquete(Long idPaquete) throws ExcepcionEnviosBios {
    Paquete p = repositorioPaquete.findById(idPaquete).orElse(null);

        if (p == null) {
            throw new ExcepcionNoExiste("El paquete no existe.");
        }

        try {

            repositorioPaquete.deleteById(idPaquete);

        } catch (DataIntegrityViolationException e) {
            throw new ExcepcionTieneVinculos("El paquete tiene clientes.");
        }
  }
  

  /*
   * @Override
   * public List<Paquete> buscarPaquetesPorCedulaCliente(String cedulaCliente) {
   * // Lo hago después
   * throw new
   * UnsupportedOperationException("Unimplemented method 'buscarPaquetesPorCedulaCliente'"
   * );
   * }
   * 
   * @Override
   * public List<Paquete> filtrarPaquetesPorEstado(String estado) {
   * // Lo hago después
   * throw new
   * UnsupportedOperationException("Unimplemented method 'filtrarPaquetesPorEstado'"
   * );
   * }
   */
}
