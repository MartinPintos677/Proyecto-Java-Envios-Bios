package com.example.envios_bios.excepciones;

public class ExcepcionTieneVinculos extends ExcepcionEnviosBios{
    
    public ExcepcionTieneVinculos(String mensaje){
        super(mensaje);
    }

    public ExcepcionTieneVinculos(String mensaje, Exception exceptionInterna){
        super(mensaje, exceptionInterna);
    }
}
