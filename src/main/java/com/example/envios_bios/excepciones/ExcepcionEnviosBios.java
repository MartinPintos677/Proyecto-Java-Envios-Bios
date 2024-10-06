package com.example.envios_bios.excepciones;

public class ExcepcionEnviosBios  extends Exception{
    
    public ExcepcionEnviosBios(String mensaje){
        super(mensaje);
    }

    public ExcepcionEnviosBios(String mensaje, Exception exceptionInterna){
        super(mensaje, exceptionInterna);
    }
}
