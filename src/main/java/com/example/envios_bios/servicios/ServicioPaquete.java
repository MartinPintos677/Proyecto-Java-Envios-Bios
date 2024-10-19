package com.example.envios_bios.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.example.envios_bios.repositorio.IRepositorioClientes;
import com.example.envios_bios.repositorio.IRepositorioPaquete;

@Service
public class ServicioPaquete implements IServicioPaquete {

  @Autowired
  private IRepositorioPaquete repositorioPaquete;

  @Autowired
  private IRepositorioClientes repositorioCliente;

  @Override
  public Paquete obtenerPaquetePorId(Long idPaquete) {
    Optional<Paquete> paqueteOpt = repositorioPaquete.findById(idPaquete);
    return paqueteOpt.orElse(null);
  }

  @Override
  public void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
    
      repositorioPaquete.save(paquete);// la guardamos en la BD
    
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
    LocalDateTime fechaInicio = null;
    LocalDateTime fechaFin = null;

    // Convertir la fecha proporcionada (si existe) a un rango de inicio y fin del
    // día
    if (fechaRegistro != null && !fechaRegistro.trim().isEmpty()) {
      try {
        LocalDate fecha = LocalDate.parse(fechaRegistro);
        fechaInicio = fecha.atStartOfDay(); // Inicio del día
        fechaFin = fecha.atTime(23, 59, 59); // Fin del día
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Fecha inválida: " + fechaRegistro, e);
      }
    }

    // Si se proporciona cédula, estado de rastreo y fecha
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() && estadoRastreo != null
        && !estadoRastreo.trim().isEmpty() && fechaInicio != null) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionAndCliente_CedulaAndFechaHoraRegistroBetween(
          estadoRastreo, cedulaCliente, fechaInicio, fechaFin, pageable);
    }

    // Si se proporciona cédula y estado de rastreo
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() && estadoRastreo != null
        && !estadoRastreo.trim().isEmpty()) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionAndCliente_Cedula(
          estadoRastreo, cedulaCliente, pageable);
    }

    // Si se proporciona cédula y fecha
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() && fechaInicio != null) {
      return repositorioPaquete.findByCliente_CedulaAndFechaHoraRegistroBetween(
          cedulaCliente, fechaInicio, fechaFin, pageable);
    }

    // Si solo se proporciona cédula
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty()) {
      return repositorioPaquete.findByCliente_Cedula(cedulaCliente, pageable);
    }

    // Si se proporciona estado de rastreo y fecha
    if (estadoRastreo != null && !estadoRastreo.trim().isEmpty() && fechaInicio != null) {
      return repositorioPaquete.findByEstadoRastreo_DescripcionAndFechaHoraRegistroBetween(
          estadoRastreo, fechaInicio, fechaFin, pageable);
    }

    // Si solo se proporciona estado de rastreo
    if (estadoRastreo != null && !estadoRastreo.trim().isEmpty()) {
      return repositorioPaquete.findByEstadoRastreo_Descripcion(estadoRastreo, pageable);
    }

    // Si solo se proporciona fecha
    if (fechaInicio != null) {
      return repositorioPaquete.findByFechaHoraRegistroBetween(fechaInicio, fechaFin, pageable);
    }

    // Si no se aplican filtros, retornar todos los paquetes
    return repositorioPaquete.findAll(pageable);
  }

}