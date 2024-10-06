package com.example.envios_bios.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Categoria;
import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.EstadoRastreo;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;

@Service
public class ServicioPaquete implements IServicioPaquete{
    
    private List<Paquete> paquetes;
  private List<Cliente> clientes; // para probar listado en dropDownList

  public ServicioPaquete() {
    paquetes = new ArrayList<>();
    clientes = new ArrayList<>(); // para probar listado en dropDownList

    // Creación de clientes
    Cliente cliente1 = new Cliente("nombreUsuario1", "clave1", "email1@example.com", "12345678", "Domicilio1",
        "099111111",true);
    Cliente cliente2 = new Cliente("nombreUsuario2", "clave2", "email2@example.com", "87654321", "Domicilio2",
        "099222222",true);

    clientes.add(cliente1);
    clientes.add(cliente2);

    // Creación de categorías y estados de rastreo
    Categoria categoria1 = new Categoria(1, "Categoria1");
    Categoria categoria2 = new Categoria(2, "Categoria2");
    EstadoRastreo estado1 = new EstadoRastreo(1, "Registrado",true);
    EstadoRastreo estado2 = new EstadoRastreo(2, "En tránsito",true);

//     // Creación de paquetes con los objetos adecuados
//     paquetes.add(new Paquete(1, cliente1, "Destinatario1", "099111111", LocalDateTime.now(), "Direccion1", false,
//         categoria1, estado1));

//     paquetes.add(new Paquete(2, cliente2, "Destinatario2", "099222222", LocalDateTime.now(), "Direccion2", true,
//         categoria2, estado2));
  }

  // Método para obtener la lista de clientes
  @Override
  public List<Cliente> listarClientes() {
    return new ArrayList<>(clientes);
  }

  @Override
  public List<Paquete> buscar(String criterio, String cedulaCliente, String fechaRegistro, String estadoRastreo) {
    return paquetes.stream()
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
    return new ArrayList<>(paquetes);
  }

  @Override
  public Paquete obtenerPaquetePorId(Long idPaquete) {
    for (Paquete p : paquetes) {
      if (p.getIdPaquete().equals(idPaquete)) {
        return p;
      }
    }
    return null;
  }

  private int obtenerPosicion(Long idPaquete) {
    for (int i = 0; i < paquetes.size(); i++) {
      if (paquetes.get(i).getIdPaquete().equals(idPaquete)) {
        return i;
      }
    }
    return -1; // No se encontró la posición
  }

  @Override
  public void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
    int posicion = obtenerPosicion(paquete.getIdPaquete());
    if (posicion != -1) {
      throw new ExcepcionYaExiste("El paquete con ID " + paquete.getIdPaquete() + " ya existe.");
    }
    paquetes.add(paquete);
  }

  @Override
  public void modificarPaquete(Paquete paquete) throws ExcepcionEnviosBios {
    int posicion = obtenerPosicion(paquete.getIdPaquete());
    if (posicion == -1) {
      throw new ExcepcionEnviosBios("El paquete a modificar no existe.");
    }
    paquetes.set(posicion, paquete);
  }

  @Override
  public void eliminarPaquete(Long idPaquete) throws ExcepcionEnviosBios {
    int posicion = obtenerPosicion(idPaquete);
    if (posicion == -1) {
      throw new ExcepcionEnviosBios("El paquete a eliminar no existe.");
    }
    paquetes.remove(posicion);
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
