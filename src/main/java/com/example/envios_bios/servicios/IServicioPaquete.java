package com.example.envios_bios.servicios;

import java.util.List;
import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServicioPaquete {

    void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios;

    Page<Paquete> buscarPorIdClienteYIdPaquete(String nombreUsuario, Long idPaquete, Pageable pageable);

    Paquete obtenerPaquetePorId(Long idPaquete);

    Page<Paquete> buscarConPaginacion(Long idPaquete, String cedulaCliente, String fechaRegistro, String estadoRastreo,
            Pageable pageable);

    Page<Paquete> listarPaquetesCliente(String cliente, String destinatario, Pageable pageable);

    void eliminarPaquete(Long idPaquete) throws ExcepcionEnviosBios;

    void modificarPaquete(Paquete paquete) throws ExcepcionEnviosBios;

    List<Paquete> listarTodosLosPaquetes();

    List<Cliente> listarClientes();
}
