package com.example.envios_bios.servicios;

import java.util.List;

import com.example.envios_bios.dominio.Cliente;
import com.example.envios_bios.dominio.Paquete;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IServicioPaquete {

    void agregarPaquete(Paquete paquete) throws ExcepcionEnviosBios;

    Paquete obtenerPaquetePorId(Long idPaquete);

    Page<Paquete> buscarConPaginacion(String cedulaCliente, String fechaRegistro, String estadoRastreo,
            Pageable pageable);

    void eliminarPaquete(Long idPaquete) throws ExcepcionEnviosBios;

    void modificarPaquete(Paquete paquete) throws ExcepcionEnviosBios;

    List<Paquete> listarTodosLosPaquetes();

    List<Cliente> listarClientes();
}
