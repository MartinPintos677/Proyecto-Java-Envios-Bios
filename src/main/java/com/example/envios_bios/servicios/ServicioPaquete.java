package com.example.envios_bios.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  public Page<Paquete> buscarConPaginacion(Long idPaquete, String cedulaCliente, String fechaRegistro,
      String estadoRastreo,
      Pageable pageable) {
    LocalDateTime fechaInicio = null;
    LocalDateTime fechaFin = null;

    // Si se proporciona un idPaquete, buscar directamente por ID y retornar un solo
    // paquete
    if (idPaquete != null) {
      Optional<Paquete> paqueteOpt = repositorioPaquete.findById(idPaquete);
      if (paqueteOpt.isPresent()) {
        // Convertimos el resultado en una página con un solo paquete para mantener la
        // estructura
        return new PageImpl<>(List.of(paqueteOpt.get()), pageable, 1);
      } else {
        // Si no se encuentra el paquete, retornar una página vacía
        return Page.empty();
      }
    }

    // Manejo de la fecha de registro: convertir el string a LocalDate si está
    // presente
    if (fechaRegistro != null && !fechaRegistro.trim().isEmpty()) {
      try {
        LocalDate fecha = LocalDate.parse(fechaRegistro); // Convierte la fecha
        fechaInicio = fecha.atStartOfDay(); // Establece la hora de inicio
        fechaFin = fecha.atTime(23, 59, 59); // Establece la hora de fin
      } catch (DateTimeParseException e) {
        // Lanza una excepción amigable si la fecha no es válida
        throw new IllegalArgumentException("El formato de la fecha es incorrecto: " + fechaRegistro, e);
      }
    }

    // Si hay cédula, estado de rastreo y fecha de registro
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() &&
        estadoRastreo != null && !estadoRastreo.trim().isEmpty() &&
        fechaInicio != null && fechaFin != null) {

      return repositorioPaquete.findByEstadoRastreo_DescripcionAndCliente_CedulaAndFechaHoraRegistroBetween(
          estadoRastreo, cedulaCliente, fechaInicio, fechaFin, pageable);
    }

    // Si hay cédula y estado de rastreo pero no hay fecha
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() &&
        estadoRastreo != null && !estadoRastreo.trim().isEmpty()) {

      return repositorioPaquete.findByEstadoRastreo_DescripcionAndCliente_Cedula(
          estadoRastreo, cedulaCliente, pageable);
    }

    // Si hay cédula y fecha de registro
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty() &&
        fechaInicio != null && fechaFin != null) {

      return repositorioPaquete.findByCliente_CedulaAndFechaHoraRegistroBetween(
          cedulaCliente, fechaInicio, fechaFin, pageable);
    }

    // Si solo hay cédula
    if (cedulaCliente != null && !cedulaCliente.trim().isEmpty()) {
      return repositorioPaquete.findByCliente_Cedula(cedulaCliente, pageable);
    }

    // Si hay estado de rastreo y fecha de registro
    if (estadoRastreo != null && !estadoRastreo.trim().isEmpty() &&
        fechaInicio != null && fechaFin != null) {

      return repositorioPaquete.findByEstadoRastreo_DescripcionAndFechaHoraRegistroBetween(
          estadoRastreo, fechaInicio, fechaFin, pageable);
    }

    // Si solo hay estado de rastreo
    if (estadoRastreo != null && !estadoRastreo.trim().isEmpty()) {
      return repositorioPaquete.findByEstadoRastreo_Descripcion(estadoRastreo, pageable);
    }

    // Si solo hay fecha de registro
    if (fechaInicio != null && fechaFin != null) {
      return repositorioPaquete.findByFechaHoraRegistroBetween(fechaInicio, fechaFin, pageable);
    }

    // Si no se aplican filtros, retornar todos los paquetes
    return repositorioPaquete.findAll(pageable);
  }

  @Override
  public Page<Paquete> listarPaquetesCliente(String cliente, String destinatario, Pageable pageable) {
    if (destinatario != null && !destinatario.isEmpty()) {
      // Si se proporciona un destinatario, filtra por cliente y destinatario
      return repositorioPaquete.findByCliente_NombreUsuarioAndNombreDestinatarioContaining(cliente, destinatario,
          pageable);
    } else {
      // Si no se proporciona un destinatario, busca solo por cliente
      return repositorioPaquete.findByCliente_NombreUsuario(cliente, pageable);
    }
  }

  @Override
  public Page<Paquete> buscarPorIdClienteYIdPaquete(String nombreUsuario, Long idPaquete, Pageable pageable) {
    return repositorioPaquete.findByCliente_NombreUsuarioAndIdPaquete(nombreUsuario, idPaquete, pageable);
  }

}