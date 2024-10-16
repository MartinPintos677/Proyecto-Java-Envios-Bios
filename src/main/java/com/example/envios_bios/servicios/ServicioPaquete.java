package com.example.envios_bios.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ServicioPaquete implements IServicioPaquete {

  @Autowired
  private IRepositorioPaquete repositorioPaquete;

  @Autowired
  private IRepositorioClientes repositorioCliente;

  @Autowired
  private ServicioEstadoRastreo servicioEstadoRastreo;

  @Override
  public Paquete obtenerPaquetePorId(Long idPaquete) {
    Optional<Paquete> paqueteOpt = repositorioPaquete.findById(idPaquete);
    return paqueteOpt.orElse(null);
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
    int posicion = obtenerPosicion(idPaquete);
    if (posicion == -1) {
      throw new ExcepcionEnviosBios("El paquete a eliminar no existe.");
    }
    paquetes.remove(posicion);
  }

  @Override
  public List<Paquete> listarTodosLosPaquetes() {
    return repositorioPaquete.findAll();
  }

  @Override
  public List<Cliente> listarClientes() {
    return repositorioCliente.findByActivo(true);
  }

  @Override
  public Page<Paquete> buscarConPaginacion(String cedulaCliente, String fechaRegistro, String estadoRastreo,
      Pageable pageable) {
    List<String> estadosFiltrados = List.of("a levantar", "levantado", "en reparto", "entregado", "devuelto");

    LocalDateTime fechaInicio = null;
    LocalDateTime fechaFin = null;
    if (fechaRegistro != null && !fechaRegistro.trim().isEmpty()) {
      try {
        LocalDate fecha = LocalDate.parse(fechaRegistro);
        fechaInicio = fecha.atStartOfDay(); // Inicio del día
        fechaFin = fecha.atTime(23, 59, 59); // Final del día
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Fecha inválida: " + fechaRegistro, e);
      }
    }

    // Aplica los filtros con las fechas convertidas
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() && fechaInicio != null) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionInAndCliente_CedulaAndFechaHoraRegistroBetween(
          estadosFiltrados, cedulaCliente, fechaInicio, fechaFin, pageable);
    } else if (cedulaCliente != null && !cedulaCliente.trim().isEmpty()) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionInAndCliente_Cedula(
          estadosFiltrados, cedulaCliente, pageable);
    } else if (fechaInicio != null) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionInAndFechaHoraRegistroBetween(
          estadosFiltrados, fechaInicio, fechaFin, pageable);
    } else if (estadoRastreo != null && !estadoRastreo.trim().isEmpty()) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionIn(estadosFiltrados, pageable);
    } else {
      return repositorioPaquete.findAll(pageable);
    }
  }

}
