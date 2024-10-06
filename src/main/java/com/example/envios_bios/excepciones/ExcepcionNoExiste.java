package com.example.envios_bios.excepciones;

public class ExcepcionNoExiste extends ExcepcionEnviosBios{
    
    public ExcepcionNoExiste(String mensaje){
        super(mensaje);
    }

    public ExcepcionNoExiste(String mensaje, Exception exceptionInterna){
        super(mensaje, exceptionInterna);
    }
}
