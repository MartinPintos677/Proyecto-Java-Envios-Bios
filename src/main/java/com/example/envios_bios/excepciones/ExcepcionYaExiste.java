package com.example.envios_bios.excepciones;

public class ExcepcionYaExiste extends ExcepcionEnviosBios{
    
    public ExcepcionYaExiste(String mensaje){
        super(mensaje);
    }

    public ExcepcionYaExiste(String mensaje, Exception exceptionInterna){
        super(mensaje, exceptionInterna);
    }
    
}
