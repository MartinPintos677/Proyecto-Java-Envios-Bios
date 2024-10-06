package com.example.envios_bios.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.envios_bios.dominio.Empleado;
import com.example.envios_bios.excepciones.ExcepcionEnviosBios;
import com.example.envios_bios.excepciones.ExcepcionNoExiste;
import com.example.envios_bios.excepciones.ExcepcionYaExiste;

@Service
public class ServicioEmpleado implements IServicioEmpleado{
    
    private IServicioSucursales servicioSucursal;
    
    private List<Empleado> empleados;

    public ServicioEmpleado(IServicioSucursales servicioSucursal){
        this.servicioSucursal = servicioSucursal;
        empleados = new ArrayList<>();

        //datos de prueba
        empleados.add(new Empleado("emp1","123","emp1@gmail.com", servicioSucursal.obtener(1L)));
        empleados.add(new Empleado("emp2","456","emp2@gmail.com", servicioSucursal.obtener(2L)));
        empleados.add(new Empleado("emp3","789","emp3@gmail.com", servicioSucursal.obtener(3L)));
        empleados.add(new Empleado("emp4","012","emp4@gmail.com", servicioSucursal.obtener(4L)));
        empleados.add(new Empleado("emp5","582","emp5@gmail.com", servicioSucursal.obtener(5L)));
        
    }
    @Override
    public void agregar(Empleado empleado) throws ExcepcionEnviosBios {
        
        if (obtener(empleado.getNombreUsuario()) != null) {
            throw new ExcepcionYaExiste("El empleado ya existe");
        }  

        empleado.setSucursal(servicioSucursal.obtener(empleado.getSucursal().getNumero()));
        empleados.add(empleado);        
    }

    @Override
    public List<Empleado> buscar(String criterio) {
        
        if(criterio == null || criterio.isBlank()) { 
            return listar();
        }
        criterio = criterio.trim().toLowerCase();

        List<Empleado> empleados = new ArrayList<>();

        for (Empleado e : this.empleados) {
            //ver si agregamos otro campo para buscar
            if (e.getNombreUsuario().toString().equals(criterio)) {
                empleados.add(e);
            }
        }

        return empleados;
    }

    @Override
    public void eliminar(String nombreUsuario) throws ExcepcionEnviosBios {
        
        int posicion = obtenerPosicion(nombreUsuario);

        if (posicion == -1) {
            throw new ExcepcionNoExiste("El empleado no existe.");
        }

        empleados.remove(posicion);
        
    }

    @Override
    public List<Empleado> listar() {
        
        return new ArrayList<>(empleados);
    }

    @Override
    public void modificar(Empleado empleado) throws ExcepcionEnviosBios {
        
        int posicion = obtenerPosicion(empleado.getNombreUsuario());

        if (posicion == -1) {
            throw new ExcepcionNoExiste("El empleado no existe.");
        }

        empleado.setSucursal(servicioSucursal.obtener(empleado.getSucursal().getNumero()));
        
        empleados.set(posicion, empleado);
        
    }

    @Override
    public Empleado obtener(String nombreUsuario) {
        Empleado empleadoEncontrado = null;

        for (Empleado e : empleados){
            if (e.getNombreUsuario().equals(nombreUsuario)){ 
                empleadoEncontrado = e;
                break;
            }
        }
        return empleadoEncontrado;
    }

    private int obtenerPosicion(String nombreUsuario) {
    int posicion = -1;  // Inicializa la posición como -1 (no encontrado)

    // Recorre la lista de empleados
    for (int i = 0; i < empleados.size(); i++) {
        // Compara el nombre de usuario actual con el nombre pasado como parámetro
        if (empleados.get(i).getNombreUsuario().equalsIgnoreCase(nombreUsuario)) {  // Usamos equalsIgnoreCase para ignorar mayúsculas/minúsculas
            posicion = i;  // Si lo encuentra, guarda la posición
            break;  
        }
    }

    return posicion;  
}
}
